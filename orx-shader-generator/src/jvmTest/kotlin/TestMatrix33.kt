@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Matrix33
import org.openrndr.math.Matrix44

class TestMatrix33 : AbstractApplicationTestFixture() {
    @Test
    fun matrix33Functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val mat by Matrix33.fromColumnVectors(v_viewPosition, v_viewPosition, v_viewPosition)
                val inv by mat.inversed
                val det by mat.determinant
                val mul by mat * mat
                val mul2 by mat * 2.0
                val mul3 by mat * v_viewPosition
                val v0 by mat[0]
                val v1 by mat[1]
                val v2 by mat[2]
                mat[0] = v_viewPosition
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

