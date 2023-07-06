import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.sharedMemoryArray
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.sharedMemoryBarrier
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.sharedMemoryVariable

fun main() {
    application {
        program {
            val cs = computeStyle {
                computeTransform {
                    var sharedVar by sharedMemoryVariable<Int>()
                    val sharedArray by sharedMemoryArray<Int>(10)
                    sharedMemoryBarrier {
                        doIf(c_giid.x eq 0U) {
                            sharedVar = 4.symbol
                            sharedArray[c_giid.x.int] = 4.symbol
                        }
                    }
                }
            }
            cs.execute(2, 1, 1)
        }
    }
}