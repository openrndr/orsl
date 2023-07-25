package minivoxel

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector3

fun ShaderBuilder.voxelMarch(
    voxelMips: ArraySymbol<RImage3D>,
    origin: Symbol<Vector3>,
    direction: Symbol<Vector3>,
    maxDistance: Symbol<Double> = 1E10.symbol,
    maxSteps: Int = 20) : Symbol<Double> {

    val marchShadow by function<Vector3, Vector3, Double, Double> { origin, direction, maxDistance ->
        var i by variable(0)
        var occ by variable(1.0)
        var level by variable(2)
        val ndirection by direction.normalized
        var worldPos by variable(origin)
        var travel by variable(0.0)

        val worldToGrid by function<Vector3, Int, IntVector3> { world, level ->
            round((world + Vector3(1.0)) * voxelMips[level].size().double * 0.5).int
        }

       i.for_(0 until maxSteps) {
            val size by voxelMips[level].size()
            val stepLength by (0.5 / size.x.double)
            travel+= stepLength

           doIf(travel gte maxDistance) {
               break_()
           }

            worldPos += ndirection * stepLength

            var outOfBounds by variable(false)
            var found by variable(false)
            val k by variable(0)
            k.for_(0 until 10) {
                val size by voxelMips[level].size()

                val gridPos by worldToGrid(worldPos, level)
                doIf(
                    (gridPos.x lt 0) or
                            (gridPos.y lt 0) or
                            (gridPos.z lt 0) or
                            (gridPos.x gte size.x) or
                            (gridPos.y gte size.y) or
                            (gridPos.z gte size.z)
                ) {
                    outOfBounds = true.symbol
                    break_()
                }
                val d by voxelMips[level].load(gridPos)
                doIf(d eq 1.0) {
                    level -= 1.symbol
                    doIf(level eq -1) {
                        level = 2.symbol
                        found = true.symbol
                        break_()
                    }
                }
                doIf(d eq 0.0) {
                    level = min(2.symbol, level + 1)
                    break_()
                }
            }
            doIf(outOfBounds) {
                break_()
            }
            doIf(found) {
                occ = 0.0.symbol
                break_()
            }
        }
        occ
    }
    return marchShadow(origin, direction, maxDistance)
}