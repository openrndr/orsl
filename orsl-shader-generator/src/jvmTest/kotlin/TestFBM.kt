package org.openrndr.orsl.shadergenerator

import AbstractApplicationTestFixture
import org.junit.jupiter.api.Test
import org.openrndr.draw.*
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.billow
import org.openrndr.orsl.shadergenerator.dsl.functions.fbm
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.get
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.shape.Circle


class TestFBM : AbstractApplicationTestFixture() {
    @Test
    fun fbm11() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s1 by function<Double, Double> {
                    simplex12(Vector2(it, it))
                }
                val fs1 by fbm(s1)
                val z by fs1(va_texCoord0.x)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun billow11() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s1 by function<Double, Double> {
                    simplex12(Vector2(it, it))
                }
                val fs1 by billow(s1)
                val z by fs1(va_texCoord0.x)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun fbm12() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s2 by function<Vector2, Double> {
                    simplex12(it)
                }
                val fs2 by fbm(s2)
                val z by fs2(va_texCoord0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun billow12() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s2 by function<Vector2, Double> {
                    simplex12(it)
                }
                val fs2 by billow(s2)
                val z by fs2(va_texCoord0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun fbm13() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s3 by function<Vector3, Double> {
                    simplex13(it)
                }
                val fs3 by fbm(s3)
                val z by fs3(Vector3(va_texCoord0.x, va_texCoord0.y, va_texCoord0.x))
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun billow13() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s3 by function<Vector3, Double> {
                    simplex13(it)
                }
                val fs3 by billow(s3)
                val z by fs3(Vector3(va_texCoord0.x, va_texCoord0.y, va_texCoord0.x))
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun fbm14() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s4 by function<Vector4, Double> {
                    simplex14(it)
                }
                val fs4 by fbm(s4)
                val z by fs4(Vector4(va_texCoord0, va_texCoord0))
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun billow14() {
        val ss = shadeStyle {
            fragmentTransform {
                val va_texCoord0 by parameter<Vector2>()

                val s4 by function<Vector4, Double> {
                    simplex14(it)
                }
                val fs4 by billow(s4)
                val z by fs4(Vector4(va_texCoord0, va_texCoord0))
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

}