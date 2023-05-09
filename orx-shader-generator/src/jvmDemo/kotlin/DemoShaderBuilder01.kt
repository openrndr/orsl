import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadImage
import org.openrndr.draw.shadeStyle
import org.openrndr.drawImage
import org.openrndr.extra.shadergenerator.phrases.MyShaderPhrases
import org.openrndr.extra.shadergenerator.phrases.MyShaderPhrasesIndex
import org.openrndr.extra.shadergenerator.phrases.PhraseResolver
import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.extra.shadergenerator.phrases.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.phrases.*
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

fun main() {
    application {
        program {
            val image = drawImage(512, 512) {
                drawer.fill = ColorRGBa.GREEN
                drawer.circle(drawer.bounds.center, 200.0)
            }
            extend {
                drawer.shadeStyle = shadeStyle {
                    fragmentTransform {
                        val va_texCoord0 by parameter<Vector2>()
                        val p_time by parameter<Double>()
                        val p_texture by parameter<Sampler2D>()
                        val p_step by parameter<Vector2>()
                        val p_4 by parameter<Vector4>()
                        val p_window by parameter<Int>()
                        val d by (c_boundsPosition.xy - Vector2(0.5, 0.5)).length
                        val n by simplex13(Vector3(c_boundsPosition.xy * 10.0, p_time)) * 0.5 + 0.5
                        val sd by smoothstep(1.0, 0.9, d * 2.0) * smoothstep(0.3, 0.9, d * 2.0) * n
                        val c by p_texture[c_boundsPosition.xy]


                        val f by function<IntVector2, _> {
                            val step by 1.0 / p_texture.size(0)
                            p_texture[va_texCoord0 + it * 0.1 * step * cos(p_time)]
                        }
                        val w by function<IntVector2, _> { (it * 1.0).length }
                        val s by ((-p_window until p_window + 1) * (-p_window until p_window + 1)).weightedAverageBy(f, w)
                        x_fill = s //Vector4(sd, sd, sd, 1.0) * c

//                        val f by function<Int, Vector4> {
//                            val g by function<Int, Vector4> { p_4 }
//                            //p_texture[p_step * it + va_texCoord0]
//                            (-p_window until p_window + 1).sumBy(g)
//                        }
//                        val b by (-p_window until p_window + 1).sumBy(f)
//                        x_fill = b
//                        x_fill = Vector4(sd, sd, sd, 1.0) * c
                    }
//                    println(fragmentPreamble)
                    parameter("time", seconds)
                    parameter("texture", image)
                    parameter("4", Vector4.UNIT_W)
                    parameter("step", Vector2.ZERO)
                    parameter("window", 1)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}