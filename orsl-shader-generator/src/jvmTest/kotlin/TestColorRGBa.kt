
@file:Suppress("LocalVariableName")
package org.openrndr.orsl.shadergenerator

import AbstractApplicationTestFixture
import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Vector4
import org.openrndr.math.Vector3
import org.openrndr.color.ColorRGBa
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol

class TestColorRGBa : AbstractApplicationTestFixture() {
    @Test
    fun functions() {
        val ss = shadeStyle {
            fragmentTransform {
                val c0 by ColorRGBa.PINK
                val c1 by ColorRGBa.RED

                val crgb by ColorRGBa(Vector3.ONE.symbol)
                val crgba by ColorRGBa(Vector4.ONE.symbol)
                val cm by c0.mix(c1, 0.5.symbol)
                val csh by c0.shade(0.5.symbol)
                val co by c0.opacify(0.5.symbol)
                val v4 by c0.vector4
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }
}

