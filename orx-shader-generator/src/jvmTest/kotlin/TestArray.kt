@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import kotlin.test.assertEquals

class TestArray : AbstractApplicationTestFixture() {
    @Test
    fun arrayTake() {
        val floats = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val ss = shadeStyle {
            fragmentTransform {
                val p_floats by arrayParameter<Double>(floats.size)
                val na by p_floats.take(3)
                assertEquals(3, na.length)
                val z by na[0]
            }
            parameter("floats", floats)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun arrayTakeLast() {
        val floats = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val ss = shadeStyle {
            fragmentTransform {
                val p_floats by arrayParameter<Double>(floats.size)
                val na by p_floats.takeLast(3)
                assertEquals(3, na.length)
                val z by na[0]
            }
            parameter("floats", floats)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun arrayDrop() {
        val floats = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val ss = shadeStyle {
            fragmentTransform {
                val p_floats by arrayParameter<Double>(floats.size)
                val na by p_floats.drop(3)
                assertEquals(1, na.length)
                val z by na[0]
            }
            parameter("floats", floats)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun arrayDropLast() {
        val floats = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val ss = shadeStyle {
            fragmentTransform {
                val p_floats by arrayParameter<Double>(floats.size)
                val na by p_floats.dropLast(3)
                assertEquals(1, na.length)
                val z by na[0]
            }
            parameter("floats", floats)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun arrayMap() {
        val floats = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
        val ss = shadeStyle {
            fragmentTransform {
                val p_floats by arrayParameter<Double>(floats.size)

                val times2 by function<Double, Double> {
                    it * 2.0
                }
                val na by p_floats.map(times2)
                assertEquals(4, na.length)
                val z by na[0]
            }
            parameter("floats", floats)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

}

