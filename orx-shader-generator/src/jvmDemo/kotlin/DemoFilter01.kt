import org.openrndr.application
import org.openrndr.draw.Filter1to1
import org.openrndr.draw.createEquivalent
import org.openrndr.drawImage
import org.openrndr.extra.shadergenerator.dsl.filter.buildFilterShader
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.math.Vector4

fun main() = application {
    program {
        class BlurFilter : Filter1to1(shader = buildFilterShader {
            var sum by variable(Vector4.ZERO.symbol)
            val j by variable<Int>()
            var weight by variable(0.0.symbol)
            val step by 1.0 / tex0.size(0)
            j.for_(-1 until 2) {
                val i by variable<Int>()
                i.for_(-1 until 2) {
                    sum += tex0[v_texCoord0 + step * IntVector2(i, j) * 4.0]
                    weight += 1.0
                }
            }
            o_output = sum * (1.0 / weight)
        }) {

        }

        val input = drawImage(width, height) {
            drawer.circle(drawer.bounds.center, 100.0)
        }
        val blurred = input.createEquivalent()

        val blurFilter = BlurFilter()

        extend {
            blurFilter.apply(input, blurred)
            drawer.image(blurred)
        }
    }
}