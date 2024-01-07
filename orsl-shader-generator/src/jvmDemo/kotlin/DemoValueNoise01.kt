import org.openrndr.application
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.camera.Camera2D
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform

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
                        val d by value13D(Vector3(va_texCoord0, p_time)*10.0).yzw * 0.5 + Vector3(0.5)
                        x_fill = Vector4(d, 1.0)
                    }
                    parameter("time", seconds * 0.1)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}