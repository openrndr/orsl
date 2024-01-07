@file:Suppress("LocalVariableName")

package org.openrndr.orsl.extension.gradient
import org.openrndr.orsl.extension.gradient.functions.jacobian

import org.junit.jupiter.api.Test
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform

import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.draw.shadeStyle

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

