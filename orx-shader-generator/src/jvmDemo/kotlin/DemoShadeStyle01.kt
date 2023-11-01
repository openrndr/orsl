import org.openrndr.application
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.math.Vector4

fun main() {
    application {
        program {
            val ss = shadeStyle {
                vertexTransform {
                    var v_something by varyingOut<Vector4>()
                    v_something = Vector4(1.0, 0.0, 0.0, 1.0).symbol
                }
                fragmentTransform {
                    val v_something by varyingIn<Vector4>()
                    x_fill = v_something
                }
            }
            extend {
                drawer.shadeStyle = ss
                drawer.circle(drawer.bounds.center, 100.0)
            }
        }
    }
}