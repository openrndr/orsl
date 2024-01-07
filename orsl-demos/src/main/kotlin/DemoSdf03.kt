import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.gradient.functions.gradient
import org.openrndr.orsl.extension.sdf.functions.march
import org.openrndr.orsl.extension.sdf.functions.position

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

                    val smin by function<Double, Double, Double> { a, bparam ->
                        val k by cos(p_time) * 0.2 + 0.25
                        val h by max(k - abs(a - bparam), 0.0) / k
                        min(a, bparam) - h * h * h * k * (1.0 / 6.0)
                    }

                    val scene by function<Vector3, Double> {
                        // the generator doesn't support closures, abuse globals instead
                        var g_position by global<Vector3>()
                        g_position = it

                        val gd by function<IntVector2, Double> {
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
                            sdSphere(coord, radius * 0.8)
                        }

                        val initialDistance by 10.0
                        val window by 1

                        val radius by 1.0
                        // find smooth minimum in 3x3 grid
                        val d by ((-window..window) * (-window..window)).foldBy(initialDistance, smin, gd)
                        val d2 by sdSphere(it, radius * 50.0)
                        min(d, -d2)
                    }

                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val normal by sceneNormal(result.position).normalized
                    val shade = normal.dot(Vector3(0.0, 0.0, 1.0))
                    x_fill = Vector4(shade, shade, shade, 1.0)
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