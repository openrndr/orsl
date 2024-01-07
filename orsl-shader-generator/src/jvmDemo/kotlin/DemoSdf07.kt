import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.gradient

import org.openrndr.orsl.shadergenerator.phrases.sdf.*
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix

// attempt to decouple the organisation of materials from the distance functions

fun main() {
    application {
        configure {
            width = 800
            height = 800
        }
        program {

            val ss = shadeStyle {
                fragmentTransform {
                    val p_origin by parameter<Vector3>()
                    val p_time by parameter<Double>()
                    val va_texCoord0 by parameter<Vector2>()
                    val p_viewMatrix by parameter<Matrix44>()
                    val fixedCoord = Vector2(va_texCoord0.x, 1.0 - va_texCoord0.y)

                    val rayDir by (p_viewMatrix * Vector4(fixedCoord - Vector2(0.5), -1.0, 0.0)).xyz.normalized
                    val rayOrigin by p_origin

                    var matQuantize by global<Boolean>()
                    var f_matQuantize by global<Boolean>()

                    var matColor by global<Vector3>()
                    var f_matColor by global<Vector3>()

                    var matSpecular by global<Double>()
                    var f_matSpecular by global<Double>()


                    // this is a Kotlin function that produces a shader function
                    // the evaluation of shade is static
                    fun scene(shade: Boolean) = function<Vector3, Double> {
                        val min by function<Double, Double, Double> { a, b ->
                            if (shade) {
                                doIf(b lt a) {
                                    f_matQuantize = matQuantize
                                    f_matSpecular = matSpecular
                                    f_matColor = matColor
                                }
                            }
                            min(a, b)
                        }

                        val radius by 1.0

                        var coord by variable<Vector3>()
                        coord = it
                        coord = Vector3(-abs(coord.x), -abs(coord.y), coord.z)

                        coord += Vector3(
                            cos(it.z + p_time),
                            cos(it.x + p_time * 0.43),
                            cos(it.y + p_time * 0.932)
                        ) * 1.0

                        if (shade) {
                            matColor = Vector3.ZERO.symbol
                            matQuantize = false.symbol
                            matSpecular = 160.0.symbol
                        }
                        var d by variable<Double>()
                        d = min(1E6.symbol, sdSphere(coord, radius))

                        if (shade) {
                            matColor = coord.normalized * 0.5 + Vector3(0.5)
                            matQuantize = true.symbol
                            matSpecular = 160.0.symbol
                        }
                        d = min(d, sdSphere(coord, radius) + value13D(it * 3.0).x)

                        if (shade) {
                            matQuantize = cos(it.y * 1.0) lt 0.0
                            matSpecular = cos(it.x * 0.1)*50.0 + 60.0

                            matColor = run {
                                val s by (cos(it.y + p_time) * 0.5 + 0.5) * 0.5
                                Vector3(s, s, s)
                            }
                        }
                        d = min(d, -sdSphere(it, radius * 50.0))
                        d
                    }

                    // use that kotlin function to create two variations of the scene distance function
                    val shadeScene by scene(true)
                    val shadowScene by scene(false)

                    val marcher by march(shadowScene, tolerance = 1E-4, stepScale = 0.3)

                    val result by marcher(rayOrigin, rayDir)
                    // this needs to be assigned to a value otherwise shadeScene is not called
                    val d2 by shadeScene(result.position)

                    val normal by run {
                        val sceneNormal by gradient(shadowScene, 2E-3)
                        sceneNormal(result.position).normalized
                    }

                    val qnormal by if_(f_matQuantize) {
                        sphericalDistribution(normal, 24.symbol).xyz
                    } else_ {
                        normal
                    }

                    val light by Vector3(0.6, 0.6, 0.6).normalized

                    val hlf by (light - rayDir).normalized
                    val diffuse by saturate(qnormal.dot(hlf))

                    val specular by pow(saturate(qnormal.dot(hlf)), f_matSpecular) *
                            diffuse *
                            (0.04 + 0.96 * pow(saturate(1.0 + hlf.dot(rayDir)), 5.0.symbol)) * 10.0

                    val amb by run {
                        val aoCalcer by calcAO(
                            shadeScene,
                            intensity = 2.0,
                            distance = 1.0,
                            iterations = 32,
                            falloff = 0.85
                        )
                        aoCalcer(result.position, qnormal)
                    }

                    val finalColor by f_matColor * diffuse +
                            Vector3.ONE * 0.04 * amb +
                            Vector3.ONE * 3.0 * specular

                    x_fill = Vector4(finalColor * 1.0, 1.0)
                    val ip by 1.0 / 2.2
                    x_fill = pow(x_fill, Vector4(ip, ip, ip, 1.0))
                }
                parameter("time", seconds)

            }
            val o = extend(Orbital())
            extend {
                ss.parameter("viewMatrix", normalMatrix(o.camera.viewMatrix().inversed))
                ss.parameter("origin", (o.camera.viewMatrix().inversed * Vector4.UNIT_W).xyz)
                ss.parameter("time", seconds)
                drawer.defaults()
                drawer.shadeStyle = ss
                drawer.rectangle(drawer.bounds)
            }
        }
    }
}