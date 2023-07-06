import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.draw.font.BufferFlag
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.boxMesh
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.compute.execute
import org.openrndr.extra.shadergenerator.dsl.IntRImage3D
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.dsl.structs.get
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.math.cos

const val voxelSize = 128

class Voxel128 : Struct<Voxel128>() {
    val position by arrayField<Vector4>(40000)
}

val Symbol<Voxel128>.position by Voxel128::position[40000]

fun main() {
//    System.loadLibrary("renderdoc")

    application {
        configure {
            width = 1280
            height = 720
        }
        program {
            val vt = volumeTexture(voxelSize, voxelSize, voxelSize, ColorFormat.R, ColorType.SINT32_INT)
            val acb = AtomicCounterBuffer.create(1)
            acb.reset()

            val struct = Voxel128()
            val instances = structuredBuffer(struct)

            val fillSdf = computeStyle {
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val p_radius by parameter<Double>()
                    val p_dent by parameter<Vector3>()
                    val p_sdf by image<IntRImage3D>(vt, 0, ImageAccess.WRITE)

                    val position by 2.0 * (c_giid.double / Vector3(voxelSize.toDouble()) - Vector3(0.5, 0.5, 0.5))
                    //val d0 by sdSphere(position, p_radius)
                    val d0 by sdPlane(position, Vector3.UNIT_Y.symbol, 0.0.symbol)
                    val d1 by sdSphere(position - p_dent, p_radius)

                    val d by opSmoothUnion(d0, d1, 0.2.symbol)
                    //val d by opSubtraction(d1, d0)
                    //val d by opSmoothSubtraction(d1, d0, 0.2.symbol)
                    val b by if_(d lt 0.0) { (1).symbol } else_ { 0.symbol }
                    p_sdf.store(c_giid.int, b)
                }
            }

            val filterSdf = computeStyle {
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val b_instances by buffer(instances, BufferAccess.WRITE, BufferFlag.RESTRICT)
                    val b_instanceIndex by buffer(acb)
                    val p_sdf by image<IntRImage3D>(vt, 0, ImageAccess.READ)

                    doIf((c_giid.x eq 0U) or (c_giid.y eq 0U) or (c_giid.z eq 0U) or (c_giid.x eq (voxelSize - 1).toUInt()) or (c_giid.y eq (voxelSize - 1).toUInt()) or (c_giid.z eq (voxelSize - 1).toUInt())) {
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

                    val hvs = voxelSize / 2.0
                    val position by (c_giid.double) - Vector3(hvs)
                    doIf(vc eq 1 and (n lt 6)) {
                        val index by b_instanceIndex.increment(0)
                        b_instances.position[index.int] = Vector4(position, 1.0)
                    }
                }
            }

            val material = Material()
            material.color = ColorRGBa.PINK.toLinear().toVector4()
            material.roughness = 0.2
            material.metallic = 0.3
            val ss = shadeStyle {
                vertexTransform {
                    val b_instances by buffer(instances, BufferAccess.READ)
                    x_position *= b_instances.position[c_instance].w
                    x_position += b_instances.position[c_instance].xyz
                }

                fragmentTransform {
                    val p_material by parameter(material)
                    val p_cameraPosition by parameter(Vector3.ZERO)

                    val sh by function<Vector3, Vector3> { n ->
                        Vector3(0.754554516862612, 0.748542953903366, 0.790921515418539).symbol +
                                Vector3(-0.083856548007422, 0.092533500963210, 0.322764661032516).symbol * (n.y) +
                                Vector3(0.308152705331738, 0.366796330467391, 0.466698181299906).symbol * (n.z) +
                                Vector3(-0.188884931542396, -0.277402551592231, -0.377844212327557).symbol * (n.x)

                    }

                    val baseColor by p_material.color.xyz
                    val roughness by p_material.roughness
                    val linearRoughness by roughness * roughness

                    val metallic by p_material.metallic
                    val intensity by 2.0

                    val diffuseColor by (1.0 - metallic) * baseColor
                    val fBase by Vector3(0.0, 0.0, 0.0)
                    val f0 by fBase * (1.0 - metallic) + baseColor * metallic
                    val attenuation by 1.0

                    val v by -(v_worldPosition - p_cameraPosition).normalized
                    val n by v_worldNormal.normalized
                    val l by Vector3(0.0, 1.0, 0.0).normalized
                    val h by (v + l).normalized

                    val NoV by abs(n.dot(v)) + 1e-5
                    val NoL by saturate(n.dot(l))
                    val NoH by saturate(n.dot(h))
                    val LoH by saturate(l.dot(h))

                    val D by D_GGX(linearRoughness, NoH)
                    val V by V_SmithGGXCorrelated(linearRoughness, NoV, NoL)
                    val F by F_Schlick(f0, LoH)

                    val Fr by (D * V) * F
                    val Fd by diffuseColor * Fd_Burley(linearRoughness, NoV, NoL, LoH)
                    val color by ((Fd + Fr) * (intensity * attenuation * NoL)) + sh(n) / Math.PI

                    val gamma by Vector3(1.0 / 2.2, 1.0 / 2.2, 1.0 / 2.2)
                    x_fill = Vector4(pow(color, gamma), 1.0)
                }
            }
            val box = boxMesh()

            extend(Orbital())
            extend {
                drawer.clear(ColorRGBa.WHITE)
                fillSdf.parameter("radius", 0.1)
                fillSdf.parameter("dent", Vector3(cos(seconds*0.3), cos(seconds*0.231), cos(seconds*0.251)))
                fillSdf.execute(voxelSize, voxelSize, voxelSize)

                filterSdf.execute(voxelSize, voxelSize, voxelSize)
                val c = acb.read()[0]
                acb.reset()

                drawer.shadeStyle = ss
                val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                drawer.shadeStyle?.parameter("cameraPosition", cameraPosition)

                drawer.cullTestPass = CullTestPass.FRONT
                drawer.vertexBufferInstances(listOf(box), emptyList(), DrawPrimitive.TRIANGLES, c)
            }
        }
    }
}