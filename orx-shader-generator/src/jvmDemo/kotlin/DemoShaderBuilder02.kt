import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.shadeStyle
import org.openrndr.drawImage
import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.extra.shadergenerator.phrases.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.structs.Struct
import org.openrndr.extra.shadergenerator.phrases.dsl.structs.StructSymbol
import org.openrndr.math.Vector3


fun main() {
    application {
        program {
            val image = drawImage(512, 512) {
                drawer.fill = ColorRGBa.GREEN
                drawer.circle(drawer.bounds.center, 200.0)
            }
            extend {
                drawer.shadeStyle = shadeStyle {
                    @Suppress("LocalVariableName")
                    fragmentTransform {
                        println("-- START")
                        class LightStruct : Struct<LightStruct>(this@fragmentTransform){
                            val a by field<Int>()
                            val position by field<Vector3>()
                            var mut by field<Vector3>()
                            override fun create(): LightStruct {
                                return LightStruct()
                            }
                        }

                        val Light by LightStruct()
                        val b by Light.array(5)
                        val k by b[0]
                        val c by Light
                        val d by Light

                        val f by Light.Function<_, Double> {
                            val z by 1.0
                            val wap by it.a
                            it.a * 2.0
                        }

                        val v by f(c)
                        val z by c.a + 1
                        val w by d.a + 2
                        val vz by Vector3.ZERO
                        d.mut = vz
                        println("-- END")
                    }
                    parameter("time", seconds)
                    parameter("texture", image)
                    parameter("window", 1)

                    print(fragmentTransform)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}