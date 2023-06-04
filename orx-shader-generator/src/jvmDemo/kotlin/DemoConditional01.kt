import org.openrndr.application
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.camera.Camera2D
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform

fun main() {
    application {
        program {
            extend(Camera2D())
            extend {
                drawer.shadeStyle = shadeStyle {
                    @Suppress("LocalVariableName")
                    fragmentTransform {
                        val p_time by parameter<Double>()
                        val c by if_(p_time lt 4.0) {
                            val z by 0.3
                            z
                        }.elseIf(p_time lt 8.0) {
                            val z by 0.7
                            z
                        } else_ {
                            val z by 1.0
                            z
                        }
                        val i by variable<Int>(0)
                        i.for_(0 until 10) {
                            val j by variable<Int>(0)
                            j.for_(0 until 10) {
                                doIf(i * j eq 4) {
                                    break_()
                                }
                            }
                        }
                        x_fill = Vector4(c, c, c, 1.0)
                    }
                    parameter("time", seconds)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}