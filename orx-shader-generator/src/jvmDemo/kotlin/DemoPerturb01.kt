import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.shadeStyle
import org.openrndr.drawImage
import org.openrndr.extra.camera.Camera2D
import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.functions.fbm
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.times
import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.extra.shadergenerator.phrases.*
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.perturb
import org.openrndr.math.*

fun main() {
    application {
        program {
            extend(Camera2D())
            extend {
                drawer.shadeStyle = shadeStyle {
                    @Suppress("LocalVariableName")
                    fragmentTransform {
                        val p_time by parameter<Double>()
                        val va_texCoord0 by parameter<Vector2>()
                        val distort by function<Vector2, Vector2> {
                            it + Vector2(cos(it.y + p_time), sin(it.x + p_time))
                        }
                        val s2 by function<Vector2, Double> {
                            simplex13(Vector3(it, p_time))
                        }
                        val ps2 by perturb(s2, distort)

                        val c = ps2(va_texCoord0 * 10.0) * 0.5 + 0.5
                        x_fill = Vector4(c, c, c, 1.0)
                    }
                    parameter("time", seconds * 1.0)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}