import org.openrndr.application
import org.openrndr.draw.ImageAccess
import org.openrndr.draw.createEquivalent
import org.openrndr.draw.imageBinding
import org.openrndr.drawImage
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.compute.execute
import org.openrndr.extra.shadergenerator.phrases.dsl.Image2D
import org.openrndr.extra.shadergenerator.phrases.dsl.UIntVector3

fun main() {
    application {
        program {
            val input = drawImage(width, height) {
                drawer.circle(drawer.bounds.center, 100.0)
            }
            val output = input.createEquivalent()
            val cs = computeStyle {
                computeTransform {
                    val p_inputImage by parameter<Image2D>()
                    val p_outputImage by parameter<Image2D>()
                    val gl_GlobalInvocationID by parameter<UIntVector3>()
                    val coord by gl_GlobalInvocationID.int.xy
                    val c by p_inputImage.load(coord)
                    p_outputImage.store(coord, c)
                }
            }
            cs.parameter("inputImage", input.imageBinding(0, ImageAccess.READ))
            cs.parameter("outputImage", output.imageBinding(0, ImageAccess.WRITE))
            cs.execute(input.width, input.height, 1)
            extend {
                drawer.image(output)
            }
        }
    }
}