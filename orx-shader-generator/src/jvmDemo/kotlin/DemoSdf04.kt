import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix


class BlendMaterial : Struct<BlendMaterial>() {
    var distance by field<Double>()
    var color by field<Vector3>()
}
var Symbol<BlendMaterial>.color by BlendMaterial::color
var Symbol<BlendMaterial>.distance by BlendMaterial::distance

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
                        bm
                    }
                    var g_color by global<Vector3>()

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
                        val d2 by sdSphere(it, radius * 50.0)
                        min(d.distance, -d2)
                    }

                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val normal by sceneNormal(result.position).normalized
                    val shade = normal.dot(Vector3(0.0, 0.0, 1.0))
                    val finalColor by g_color * shade

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