import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Struct
import org.openrndr.draw.parameter
import org.openrndr.draw.shadeStyle
import org.openrndr.drawImage
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Vector3

class Light : Struct<Light>() {
    var position by field<Vector3>()
}

var Symbol<Light>.position by Light::position

fun main() {
    val a = Light::position
    application {
        program {
            val image = drawImage(512, 512) {
                drawer.fill = ColorRGBa.GREEN
                drawer.circle(drawer.bounds.center, 200.0)
            }
            extend {
                val light = Light()
                light.position = Vector3(1.0, 0.1, 0.3)
                drawer.shadeStyle = shadeStyle {
                    @Suppress("LocalVariableName")
                    fragmentTransform {
                        val k by Vector3.ZERO
                        val p_light by parameter<Light>()
                        val p_lights by arrayParameter<Light>(2)

                        val cmp by function<Light, Double> {
                            (it.position).length
                        }

                        val nearest by p_lights.minBy(cmp)
                        val nearestIdx by p_lights.argMinBy(cmp)

                        val z by p_lights[0].position * 1.2
                        val f by function<Light, Vector3> { it -> it.position }

                        val addLight by function<Light, Light, Light> { a, b ->
                            val sum by Light()
                            sum.position = a.position + b.position
                            sum
                        }

                        val sum by addLight(p_lights[0], p_lights[1])
                        val positions by p_lights.map(f)



                        val a by Vector4(z, 1.0)
                        x_fill = a
                    }
                    parameter("time", seconds)
                    parameter("texture", image)
                    parameter("window", 1)
                    parameter("light", light)
                    parameter("lights", arrayOf(light, light))

                    print(fragmentTransform)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}