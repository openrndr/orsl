import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.boxMesh
import org.openrndr.orsl.shadergenerator.compute.computeTransform
import org.openrndr.orsl.shadergenerator.dsl.IntRImage3D
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.get
import org.openrndr.orsl.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.math.cos

class Voxel32 : Struct<Voxel32>() {
    val position by arrayField<Vector4>(32 * 32 * 32)
}

val Symbol<Voxel32>.position by Voxel32::position[32 * 32 * 32]

fun main() {
    application {
        program {
            val vt = volumeTexture(32, 32, 32, ColorFormat.R, ColorType.SINT32_INT)

            val struct = Voxel32()
            val instances = structuredBuffer(struct)

            val fillSdf = computeStyle {
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val p_radius by parameter<Double>()
                    val p_sdf by parameter<IntRImage3D>()
                    val position by 2.0 * (c_giid.double / Vector3(32.0, 32.0, 32.0) - Vector3(0.5, 0.5, 0.5))

                    val d by sdSphere(position, p_radius)
                    val b by if_(d lt 0.0) { (1).symbol } else_ { 0.symbol }
                    p_sdf.store(c_giid.int, b)

                }
                image("sdf", vt.imageBinding(0, ImageAccess.WRITE))
            }

            val filterSdf = computeStyle {
                image("sdf", vt.imageBinding(0, ImageAccess.READ))

                buffer("instances", instances)
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val b_instances by parameter<Voxel32>()
                    val p_sdf by parameter<IntRImage3D>()

                    doIf((c_giid.x eq 0U) or (c_giid.y eq 0U) or (c_giid.z eq 0U) or (c_giid.x eq 31U) or (c_giid.y eq 31U) or (c_giid.z eq 31U)) {
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

                    val idx by c_giid.x + (c_giid.y * 32U) + (c_giid.z * 32U * 32U)
                    val position by (c_giid.double) - Vector3(16.0, 16.0, 16.0)

                    b_instances.position[idx.int] = Vector4(0.0).symbol

                    doIf(vc eq 1 and (n lt 6)) {
                        b_instances.position[idx.int] = Vector4(position, 1.0)
                    }
                }
            }

            val ss = shadeStyle {
                buffer("instances", instances)
                vertexTransform {
                    val b_instances by parameter<Voxel32>()
                    x_position *= b_instances.position[c_instance].w
                    x_position += b_instances.position[c_instance].xyz
                }

                fragmentTransform {
                    //x_fill = org.openrndr.math.Vector4(1.0, 0.0, 0.0, 1.0).symbol
                    x_fill = Vector4(v_viewNormal, 1.0)
                }
            }

            vt.imageBinding(0, ImageAccess.READ_WRITE)
            filterSdf.buffer("instances", instances)
            val box = boxMesh()
            extend(Orbital())
            extend {
                fillSdf.parameter("radius", cos(seconds)*0.5+0.5)
                fillSdf.execute(32, 32, 32)

                filterSdf.execute(32, 32, 32)

                drawer.shadeStyle = ss
                drawer.vertexBufferInstances(listOf(box), emptyList(), DrawPrimitive.TRIANGLES, 32*32*32)

            }


        }
    }
}