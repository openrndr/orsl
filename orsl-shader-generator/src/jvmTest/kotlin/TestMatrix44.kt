@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Matrix44

class TestMatrix44 : AbstractApplicationTestFixture() {
    @Test
    fun matrix44Functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val u_viewMatrix by parameter<Matrix44>()
                val inv by u_viewMatrix.inversed
                val det by u_viewMatrix.determinant
                val mul by u_viewMatrix * u_viewMatrix
                val mul2 by u_viewMatrix * 2.0
                val mul3 by u_viewMatrix * Vector4(v_viewPosition, 1.0)
                val v4 by org.openrndr.math.Vector4(1.0, 1.0, 1.0, 1.0)
                val mat by Matrix44.fromColumnVectors(v4, v4, v4, v4)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

