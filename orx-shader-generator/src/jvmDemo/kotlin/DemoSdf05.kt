import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.extra.shadergenerator.phrases.sdf.*
import org.openrndr.extra.shadergenerator.phrases.sdf.calcSoftShadow
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix


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
                    var g_position by global<Vector3>()

                    val shadowScene by function<Vector3, Double> {
                        g_position = it

                        val sd by function<IntVector2, Double> {
                            val fx by floor(g_position.x + 0.5 + it.x * -1.0)
                            val fy by floor(g_position.y + 0.5 + it.y * -1.0)
                            val fv by Vector2(fx, fy).length
                            val zOff by cos(fv)
                            val radius by (sin(fx + fy * 0.025 + p_time * 2.0) * 0.25 + 0.75)

                            val modPosition by Vector3(
                                it.x + (g_position.x + 0.5).mod(1.0) - 0.5,
                                it.y + (g_position.y + 0.5).mod(1.0) - 0.5,
                                g_position.z + zOff
                            )

                            val coord by modPosition
                            val distance = sdSphere(coord, radius * 0.8) + cos(coord.z * 10.0 + p_time + fv) * 0.1
                            distance
                        }
                        val window by 1
                        val d0 by min(((-window..window) * (-window..window)).minBy(sd), it.z + 1.5)
                        val d1 by min(d0, -(it.x) + 10.0)
                        val d2 by min(d1, (it.x) + 10.0)
                        min(d2, -it.y+10.0)
                    }


                    val marcher by march(shadowScene, tolerance = 1E-2, stepScale = 0.4)
                    val result by marcher(rayOrigin, rayDir)

                    val normal by run {
                        val sceneNormal by gradient(shadowScene, 1E-4)
                        sceneNormal(result.position).normalized
                    }

                    val light by Vector3(0.6, 0.6, 0.6).normalized
//                    val occ by run {
//                        val shadows by calcShadow(shadowScene, iterations = 32)
//                        val tmin by 0.01
//                        val tmax by 40.0
//                        shadows(result.position, light, tmin, tmax)
//                    }
                    val occ by 1.0

                    val hlf by (light - rayDir).normalized
                    val diffuse by saturate(normal.dot(hlf)) * occ

                    val d16 by 16.0
                    val d5 by 5.0

                    val specular by pow( saturate( normal.dot(hlf)), 1.0 + d16)*
                            diffuse *
                            (0.04 + 0.96*pow( saturate(1.0+hlf.dot(rayDir)), d5 ));

                    val amb by run {
                        val aoCalcer by calcAO(shadowScene, intensity = 0.5, distance = 1.0, iterations = 32, falloff = 0.85)
                        aoCalcer(result.position, normal)
                    }

                    val w by Vector3(0.5, 0.5,0.5)
                    val lw by Vector3(0.5, 0.5, 0.5)
                    val finalColor by Vector3(1.0, 1.0, 1.0) * 0.35 * diffuse + w * amb + lw * 6.0 * specular

                    x_fill = Vector4(finalColor, 1.0)


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