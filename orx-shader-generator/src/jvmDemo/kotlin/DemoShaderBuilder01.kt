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
import org.openrndr.math.*


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
                        val va_texCoord0 by parameter<Vector2>()
                        val p_time by parameter<Double>()
                        val p_texture by parameter<Sampler2D>()
                        val p_window by parameter<Int>()

                        val f by function<IntVector2, _> {
                            p_texture[va_texCoord0 + it * 0.1  * cos(p_time)]
                        }

                        val w by function<IntVector2, _> { (it * 1.0).length }
                        val s by ((-p_window .. p_window )  * (-p_window .. p_window)).weightedAverageBy(f, w)
                        x_fill = s
                    }
                    parameter("time", seconds)
                    parameter("texture", image)
                    parameter("window", 1)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}