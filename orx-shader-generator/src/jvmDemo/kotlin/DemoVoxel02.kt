import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.boxMesh
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.compute.execute
import org.openrndr.extra.shadergenerator.dsl.IntRImage3D
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.dsl.structs.get
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.internal.Driver
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.math.cos

class Voxel128 : Struct<Voxel128>() {
    val position by arrayField<Vector4>(128 * 128 * 128)
}

val Symbol<Voxel128>.position by Voxel128::position[128 * 128 * 128]

fun main() {
    System.loadLibrary("renderdoc")

    application {
        configure {
            width = 1280
            height = 720
        }
        program {
            val vt = volumeTexture(128, 128, 128, ColorFormat.R, ColorType.SINT32_INT)

            val acb = AtomicCounterBuffer.create(1)
            acb.reset()

            val struct = Voxel128()
            val instances = structuredBuffer(struct)

            val fillSdf = computeStyle {
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val p_radius by parameter<Double>()
                    val p_sdf by parameter<IntRImage3D>()
                    val position by 2.0 * (c_giid.double / Vector3(128.0, 128.0, 128.0) - Vector3(0.5, 0.5, 0.5))
                    val d by sdSphere(position, p_radius)
                    val b by if_(d lt 0.0) { (1).symbol } else_ { 0.symbol }
                    p_sdf.store(c_giid.int, b)
                }
                parameter("sdf", vt.imageBinding(0, ImageAccess.WRITE))
            }

            val filterSdf = computeStyle {
                parameter("sdf", vt.imageBinding(0, ImageAccess.READ))

                buffer("instanceIndex", acb)
                buffer("instances", instances)
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val b_instances by parameter<Voxel128>()
                    val b_instanceIndex by parameter<AtomicCounterBuffer>()
                    val p_sdf by parameter<IntRImage3D>()

                    doIf((c_giid.x eq 0U) or (c_giid.y eq 0U) or (c_giid.z eq 0U) or (c_giid.x eq 127U) or (c_giid.y eq 127U) or (c_giid.z eq 127U)) {
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

                    val position by (c_giid.double) - Vector3(64.0, 64.0, 64.0)
                    doIf(vc eq 1 and (n lt 6)) {
                        val index by b_instanceIndex.increment(0)
                        b_instances.position[index.int] = Vector4(position, 1.0)
                    }
                }
            }

            val ss = shadeStyle {
                buffer("instances", instances)
                vertexTransform {
                    val b_instances by parameter<Voxel128>()
                    x_position *= b_instances.position[c_instance].w
                    x_position += b_instances.position[c_instance].xyz
                }

                fragmentTransform {
                    x_fill = Vector4(v_viewNormal * 0.5 + Vector3(0.5), 1.0)
                }
            }

            vt.imageBinding(0, ImageAccess.READ_WRITE)
            filterSdf.buffer("instances", instances)
            val box = boxMesh()
            extend(Orbital())
            extend {
                fillSdf.parameter("radius", cos(seconds)*0.45+0.45)
                fillSdf.execute(128, 128, 128)
                filterSdf.execute(128, 128, 128)
                val c = acb.read()[0]
                acb.reset()
                drawer.shadeStyle = ss
                drawer.cullTestPass = CullTestPass.FRONT
                drawer.vertexBufferInstances(listOf(box), emptyList(), DrawPrimitive.TRIANGLES, c)
            }
        }
    }
}