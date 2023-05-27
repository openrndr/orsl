@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Vector3

class TestVector3 : AbstractApplicationTestFixture() {
    @Test
    fun vector3Functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val v0 by Vector3.ONE
                val v1 by Vector3.ZERO
                val one by 1.0
                val True by true
                val m by v0.mix(v1, one)
                val mb by v0.mix(v1, True)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

