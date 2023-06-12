@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform

class TestFor : AbstractApplicationTestFixture() {
    @Test
    fun simpleRun() {
        val ss = shadeStyle {
            fragmentTransform {
                val c by run {
                    val z by cos(c_boundsPosition.x * 10.0)
                    z
                }
                x_fill = Vector4(c, c, c, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun functionInsideRun() {
        val ss = shadeStyle {
            fragmentTransform {
                val c by run {
                    val g by function<Double, Double> {
                        it * it
                    }
                    val z by g(cos(c_boundsPosition.x * 10.0))
                    z
                }
                x_fill = Vector4(c, c, c, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}