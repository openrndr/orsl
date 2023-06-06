@file:Suppress("LocalVariableName")

import org.junit.jupiter.api.Test
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform

class TestFunctions : AbstractApplicationTestFixture() {
    @Test
    fun simpleFunction1() {
        val ss = shadeStyle {
            fragmentTransform {
                val f by function<Double, Double> {
                    it + 1.0
                }
                x_fill = Vector4(f(1.0.symbol), f(1.0.symbol), f(1.0.symbol), 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun simpleFunction1NameCollision() {
        val ss = shadeStyle {
            fragmentTransform {
                val a by run {
                    val f by function<Double, Double> {
                        it + 1.0
                    }
                    f(1.0.symbol)
                }
                val b by run {
                    val f by function<Double, Double> {
                        it + 2.0
                    }
                    f(1.0.symbol)
                }
                x_fill = Vector4(a, b, 1.0.symbol, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun simpleFunction2NameCollision() {
        val ss = shadeStyle {
            fragmentTransform {
                val a by run {
                    val f by function<Double, Double, Double> { a, b ->
                        a + b
                    }
                    f(1.0.symbol, 1.0.symbol)
                }
                val b by run {
                    val f by function<Double, Double, Double> { a, b ->
                        b + a
                    }
                    f(1.0.symbol, 1.0.symbol)
                }
                x_fill = Vector4(a, b, 1.0.symbol, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }


    @Test
    fun simpleFunction3NameCollision() {
        val ss = shadeStyle {
            fragmentTransform {
                val a by run {
                    val f by function<Double, Double, Double, Double> { a, b, c ->
                        a + b + c
                    }
                    f(1.0.symbol, 1.0.symbol, 2.0.symbol)
                }
                val b by run {
                    val f by function<Double, Double, Double, Double> { a, b, c ->
                        c + b + a
                    }
                    f(1.0.symbol, 1.0.symbol, 2.0.symbol)
                }
                x_fill = Vector4(a, b, 1.0.symbol, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

    @Test
    fun simpleFunction4NameCollision() {
        val ss = shadeStyle {
            fragmentTransform {
                val a by run {
                    val f by function<Double, Double, Double, Double, Double> { a, b, c, d ->
                        a + b + c + d
                    }
                    f(1.0.symbol, 1.0.symbol, 2.0.symbol, 5.0.symbol)
                }
                val b by run {
                    val f by function<Double, Double, Double, Double, Double> { a, b, c, d ->
                        d + c + b + a
                    }
                    f(1.0.symbol, 1.0.symbol, 2.0.symbol, 5.0.symbol)
                }
                x_fill = Vector4(a, b, 1.0.symbol, 1.0)
            }
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(program.drawer.bounds.center, 100.0)
    }

}