@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Vector4
import org.openrndr.math.Vector3
import org.openrndr.math.Vector2

class TestFragmentDerivatives : AbstractApplicationTestFixture() {
    @Test
    fun functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by 5.0.symbol
                val dx by f.dFdx()
                val dy by f.dFdy()
                val w by f.fwidth()

                val v2 by Vector2.ONE.symbol
                val dx2 by v2.dFdx()
                val dy2 by v2.dFdy()
                val w2 by v2.fwidth()

                val v3 by Vector3.ONE.symbol
                val dx3 by v3.dFdx()
                val dy3 by v3.dFdy()
                val w3 by v3.fwidth()

                val v4 by Vector4.ONE.symbol
                val dx4 by v4.dFdx()
                val dy4 by v4.dFdy()
                val w4 by v4.fwidth()

            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

