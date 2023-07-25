package minivoxel

import org.openrndr.draw.ComputeStyle
import org.openrndr.draw.ImageAccess
import org.openrndr.draw.computeStyle
import org.openrndr.draw.font.BufferFlag
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.RImage3D
import org.openrndr.math.IntVector3

fun downscaleVolume() : ComputeStyle {
    return computeStyle {
        workGroupSize = IntVector3(1, 1, 1)
        computeTransform {
            val p_input by image<RImage3D>(ImageAccess.READ, setOf(BufferFlag.COHERENT, BufferFlag.RESTRICT))
            val p_output by image<RImage3D>(ImageAccess.WRITE, setOf(BufferFlag.COHERENT, BufferFlag.RESTRICT))

            val k by variable(0)
            val j by variable(0)
            val i by variable(0)

            var maxValue by variable(-1E10)
            k.for_(0 until 2) {
                j.for_(0 until 2) {
                    i.for_(0 until 2) {
                        val idx by c_giid * 2 + IntVector3(i, j, k)
                        maxValue = max(maxValue, p_input.load(idx))
                    }
                }
            }
            p_output.store(c_giid.int, maxValue)
        }
    }
}