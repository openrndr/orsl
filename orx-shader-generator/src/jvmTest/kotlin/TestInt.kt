@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Vector4

class TestInt : AbstractApplicationTestFixture() {
    @Test
    fun functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val v0 by 0.symbol
                val v1 by 4.symbol

                val m by v0 * v1
                val n by v0 + 2

                val msb by m.findMSB()
                val lsb by m.findLSB()
                val count by m.bitCount()
                val reversed by m.bitfieldReverse()
                val extract by m.bitfieldExtract(2.symbol, 3.symbol)

                val u by v0.uint
                val d by v0.double
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

