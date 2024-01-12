import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import org.openrndr.orsl.shadergenerator.compute.computeTransform
import org.openrndr.orsl.shadergenerator.dsl.Image2D
import kotlin.math.sin

/**
 * Use a compute shader to read from a colorBuffer
 * and write into a different colorBuffer. The input colorBuffer
 * is updated on every animation frame (a moving circle).
 * Then the compute shader is run for (width x height x 1) to update
 * most pixels of the target colorBuffer by reading a displaced
 * pixel in the source colorBuffer.
 */
fun main() {
    application {
        program {
            val input = renderTarget(width, height) {
                colorBuffer()
            }
            val output = input.colorBuffer(0).createEquivalent()
            val cs = computeStyle {
                computeTransform {
                    val p_inputImage by parameter<Image2D>()
                    val p_outputImage by parameter<Image2D>()
                    val p_m by parameter<Vector2>()
                    val coord by c_giid.int.xy
                    val off by IntVector2(
                        coord.x + mod(c_giid.int.x * p_m.int.x, 10) - 5,
                        coord.y + mod(c_giid.int.y * p_m.int.y, 10) - 5
                    )
                    val c by p_inputImage.load(off)
                    p_outputImage.store(coord, c)
                }
            }
            cs.image("inputImage", input.colorBuffer(0).imageBinding(0, ImageAccess.READ))
            cs.image("outputImage", output.imageBinding(0, ImageAccess.WRITE))

            extend {
                // Update input
                drawer.isolatedWithTarget(input) {
                    clear(ColorRGBa.TRANSPARENT)
                    circle(bounds.center, 100.0 + 80 * sin(seconds))
                }

                // Apply the compute shader to transform input into output
                cs.parameter("m", Vector2(3.0 + (frameCount % 25), 3.0 + (frameCount % 27)))
                cs.execute(input.width, input.height, 1)

                // Draw result
                drawer.image(output)
            }
        }
    }
}
