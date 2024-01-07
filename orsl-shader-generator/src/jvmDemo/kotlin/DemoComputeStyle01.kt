import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.drawImage
import org.openrndr.orsl.shadergenerator.compute.computeTransform
import org.openrndr.orsl.shadergenerator.dsl.Image2D
import org.openrndr.orsl.shadergenerator.dsl.UIntVector3

fun main() {
    System.loadLibrary("renderdoc")
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
                    val coord by c_giid.int.xy
                    val c by p_inputImage.load(coord)
                    p_outputImage.store(coord, c)
                }
            }
            cs.image("inputImage", input.imageBinding(0, ImageAccess.READ))
            cs.image("outputImage", output.imageBinding(0, ImageAccess.WRITE))

            extend {
                cs.execute(input.width, input.height, 1)
                for (i in 0 until 10000    ) {
                    drawer.image(output)
                }
            }
        }
    }
}