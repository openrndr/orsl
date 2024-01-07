import org.openrndr.application
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.camera.Camera2D
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.*
import org.openrndr.orsl.extension.noise.functions.fbm
import org.openrndr.orsl.extension.noise.functions.perturb
import org.openrndr.orsl.extension.noise.phrases.simplex13
import org.openrndr.orsl.extension.noise.phrases.simplex22

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

                        val s2 by function<Vector2, Double> {
                            simplex13(Vector3(it, p_time))
                        }
                        val distort by function<Vector2, Vector2> {
                            it + simplex22(it * 0.7)
                        }
                        val ps2 by perturb(s2, distort)
                        val fps2 by fbm(ps2)

                        val c = fps2(va_texCoord0 * 10.0) * 0.5 + 0.5
                        x_fill = Vector4(c, c, c, 1.0)
                    }
                    parameter("time", seconds * 1.0)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}