import org.openrndr.application
import org.openrndr.draw.shadeStyle
import org.openrndr.extra.camera.Camera2D
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform

import org.openrndr.math.*
import org.openrndr.orsl.extension.noise.functions.fbm
import org.openrndr.orsl.extension.noise.phrases.*

fun main() {
    application {
        program {
            extend(Camera2D())
            extend {
                drawer.shadeStyle = shadeStyle {
                    @Suppress("LocalVariableName")
                    fragmentTransform(
                        SimplexPhrasesIndex(SimplexPhrases()),
                        Mod289PhrasesIndex(Mod289Phrases()),
                        PermutePhrasesIndex(PermutePhrases())
                    ) {
                        val p_time by parameter<Double>()
                        val va_texCoord0 by varyingIn<Vector2>()
                        val s2 by function<Vector2, Double> {
                            simplex13(Vector3(it, p_time))
                        }
                        val fs2 by fbm(s2, lacunarity = 1.9543, octaves = 8)
                        val c = fs2(va_texCoord0 * 10.0) * 0.5 + 0.5
                        x_fill = Vector4(c, c, c, 1.0)
                    }
                    parameter("time", seconds * 0.1)
                }
                drawer.circle(drawer.bounds.center, 200.0)
            }
        }
    }
}