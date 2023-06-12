import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.extra.shadergenerator.phrases.sdf.calcAO
import org.openrndr.extra.shadergenerator.phrases.sdf.march
import org.openrndr.extra.shadergenerator.phrases.sdf.position
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix


class BlendMaterial : Struct<BlendMaterial>() {
    var distance by field<Double>()
    var color by field<Vector3>()
    var specular by field<Double>()
}
var Symbol<BlendMaterial>.color by BlendMaterial::color
var Symbol<BlendMaterial>.distance by BlendMaterial::distance
var Symbol<BlendMaterial>.specular by BlendMaterial::specular

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

                    val sdSphere by function<Vector3, Double, Double> { p, s ->
                        p.length - s
                    }

                    val smin by function<BlendMaterial, BlendMaterial, BlendMaterial> { a, b ->
                        val k by 0.5
                        val h by max(k - abs(a.distance - b.distance), 0.0) / k
                        val m by h * h * h * .5
                        val s by m * k * (1.0 / 3.0)
                        val sf by Vector2(a.distance - s, m).mix(Vector2(b.distance - s, 1.0 - m), a.distance gte b.distance)
                        val bm by BlendMaterial()
                        bm.distance = sf.x
                        bm.color = a.color.mix(b.color, sf.y)
                        bm.specular = a.specular.mix(b.specular, sf.y)
                        bm
                    }
                    var g_color by global<Vector3>()
                    var g_specular by global<Double>()

                    val scene by function<Vector3, Double> {
                        // the generator doesn't support closures, abuse globals instead

                        var g_position by global<Vector3>()
                        g_position = it

                        val gd by function<IntVector2, BlendMaterial> {
                            val fx by floor(g_position.x + 0.5 + it.x * -1.0)
                            val fy by floor(g_position.y + 0.5 + it.y * -1.0)
                            val fv by Vector2(fx, fy).length
                            val zOff by cos(fv + p_time)
                            val radius by sin(fx + fy * 0.025 + p_time * 2.0) * 0.25 + 0.75

                            val modPosition by Vector3(
                                it.x + (g_position.x + 0.5).mod(1.0) - 0.5,
                                it.y + (g_position.y + 0.5).mod(1.0) - 0.5,
                                g_position.z + zOff
                            )

                            val coord by modPosition
                            val distance = sdSphere(coord, radius * 0.8)
                            val bm by BlendMaterial()
                            bm.distance = distance

                            val c = cos( (fx+ fy) * Math.PI * 0.5) * 0.5 + 0.5

                            bm.color = Vector3(c,c,c)
                            bm.specular = (cos( (fx+ fy) * Math.PI * 0.25) * 0.5 + 0.5) * 160.0
                            bm
                        }

                        val one by 1.0
                        val initialDistance by BlendMaterial()
                        initialDistance.distance = one * 10.0
                        initialDistance.color = Vector3(one, one, one)
                        val window by 1

                        val radius by 1.0
                        // find smooth minimum in 3x3 grid
                        val d by ((-window..window) * (-window..window)).foldBy(initialDistance, smin, gd)
                        g_color = d.color
                        g_specular = d.specular

                        val d2 by sdSphere(it, radius * 50.0)

                        d.distance.elseIf(-d2 lte d.distance) {
                            g_color = Vector3(one, one, one)
                            g_specular = one
                            -d2
                        }



                    }


                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val normal by sceneNormal(result.position).normalized

                    val light by Vector3(0.1,0.3,0.6).normalized
                    val hlf by (light- rayDir).normalized
                    val diffuse by saturate(normal.dot(hlf))

                    val d16 by 160.0
                    val d5 by 5.0
                    val specular by pow( saturate( normal.dot(hlf)), 1.0 + g_specular)*
                    diffuse *
                            (0.04 + 0.96*pow( saturate(1.0+hlf.dot(rayDir)), d5 ));

                    val aoCalcer by calcAO(scene)

                    val w by Vector3(0.5, 0.5,0.5)
                    val lw by Vector3(0.5, 0.5, 0.5)
                    val finalColor by g_color*0.65 * diffuse + w * aoCalcer(result.position, normal) * 0.5 + lw * 6.0 * specular

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