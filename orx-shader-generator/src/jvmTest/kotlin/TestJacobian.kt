@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.jacobian
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

class TestJacobian : AbstractApplicationTestFixture() {
    @Test
    fun jacobian33() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Vector3, Vector3> {
                    it * it
                }
                val g by jacobian(f)
                val z by g(v_clipPosition.xyz)
                val zd by z.determinant
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun jacobian44() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Vector4, Vector4> {
                    it
                }
                val g by jacobian(f)
                val z by g(v_clipPosition)
                val zd by z.determinant
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

}

