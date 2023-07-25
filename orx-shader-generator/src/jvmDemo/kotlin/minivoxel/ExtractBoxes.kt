package minivoxel

import Voxel128
import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.draw.font.BufferFlag
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.RImage3D
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.math.IntVector3
import position

fun extractBoxes(vt: VolumeTexture,
                 instances: StructuredBuffer<Voxel128>,
                 acb: AtomicCounterBuffer) = computeStyle {
    workGroupSize = IntVector3(1, 1, 1)
    computeTransform {
        val b_instances by buffer(instances, BufferAccess.WRITE, BufferFlag.RESTRICT)
        val b_instanceIndex by buffer(acb)
        val p_sdf by image<RImage3D>(vt, 0, ImageAccess.READ)

        val voxelSize by p_sdf.size()
        doIf((c_giid.x eq 0U) or
                (c_giid.y eq 0U) or
                (c_giid.z eq 0U) or
                (c_giid.x eq (voxelSize.x - 1).uint) or
                (c_giid.y eq (voxelSize.y - 1).uint) or
                (c_giid.z eq (voxelSize.z - 1).uint)) {
            return_()
        }

        val u by c_giid.int + IntVector3(0, -1, 0)
        val d by c_giid.int + IntVector3(0, 1, 0)
        val l by c_giid.int + IntVector3(-1, 0, 0)
        val r by c_giid.int + IntVector3(1, 0, 0)
        val f by c_giid.int + IntVector3(0, 0, -1)
        val b by c_giid.int + IntVector3(0, 0, 1)

        val vu by p_sdf.load(u)
        val vd by p_sdf.load(d)
        val vl by p_sdf.load(l)
        val vr by p_sdf.load(r)
        val vf by p_sdf.load(f)
        val vb by p_sdf.load(b)
        val vc by p_sdf.load(c_giid.int)

        val n by vu + vd + vl + vr + vf + vb

        val hvs = voxelSize.double / 2.0
        val position by (c_giid.double) - hvs
        doIf(vc eq 1.0 and (n lt 6.0)) {
            val index by b_instanceIndex.increment(0)
            b_instances.position[index.int] = Vector4(position, 1.0)
        }
    }
}