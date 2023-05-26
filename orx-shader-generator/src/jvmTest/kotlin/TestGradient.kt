@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.test.assertEquals

class TestGradient : AbstractApplicationTestFixture() {
    @Test
    fun gradient11() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Double, Double> {
                    it * it
                }
                val g by gradient(f)
                val z by g(v_clipPosition.x)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }


    @Test
    fun gradient12() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Vector2, Double> {
                    it.x * it.y
                }
                val g by gradient(f)
                val z by g(v_clipPosition.xy)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun gradient13() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Vector3, Double> {
                    it.x * it.y * it.z
                }
                val g by gradient(f)
                val z by g(v_clipPosition.xyz)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun gradient14() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Vector4, Double> {
                    it.x * it.y * it.z * it.w
                }
                val g by gradient(f)
                val z by g(v_clipPosition)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

