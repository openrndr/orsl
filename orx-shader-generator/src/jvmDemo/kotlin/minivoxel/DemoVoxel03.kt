package minivoxel

import Material
import Voxel128
import color
import metallic
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.draw.font.BufferFlag
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.boxMesh
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.RImage3D
import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.setSeed
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.uniformBall
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.uniformSphere
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import position
import roughness
import voxelSize
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    System.loadLibrary("renderdoc")

    application {
        configure {
            width = 1280
            height = 720
        }
        program {
            val rt = renderTarget(width, height, 1.0) {
                colorBuffer("albedo", ColorFormat.RGBa)
                colorBuffer("normal", ColorFormat.RGB, ColorType.FLOAT16)
                colorBuffer("worldPosition", ColorFormat.RGB, ColorType.FLOAT16)
                colorBuffer("material", ColorFormat.RGBa, ColorType.UINT8)
                colorBuffer("viewZ", ColorFormat.R, ColorType.FLOAT32)
                depthBuffer()
            }

            val vt = volumeTexture(
                voxelSize,
                voxelSize,
                voxelSize,
                format = ColorFormat.R,
                type = ColorType.UINT8,
                levels = 3
            )

            val downscale = downscaleVolume()

            val acb = AtomicCounterBuffer.create(1)
            acb.reset()

            val struct = Voxel128()
            val instances = structuredBuffer(struct)

            val fillSdf = computeStyle {
                workGroupSize = IntVector3(1, 1, 1)
                computeTransform {
                    val p_radius by parameter<Double>()
                    val p_dent by parameter<Vector3>()
                    val p_sdf by image<RImage3D>(vt, 0, ImageAccess.WRITE)

                    val position by 2.0 * (c_giid.double / Vector3(voxelSize.toDouble()) - Vector3(0.5))
                    //val d0 by sdSphere(position, p_radius)
                    val d0 by -sdBox(position, Vector3(0.9, 0.9, 0.9).symbol)
                    //val d0 by -sdSphere(position, 1.05.symbol)
                    val d1 by sdSphere(position + Vector3(0.0, -0.3, 0.0), p_radius)
                    val d2 by sdSphere(position + Vector3(0.0, 0.3, 0.0), p_radius)
                    val d by opSmoothUnion(d0, opSmoothUnion(d1, d2, 0.3.symbol), 0.3.symbol)
                    //val by d if_(position.x lt 0.0) { (1).symbol } else_ {0.symbol }
                    //val d by opSmoothUnion(d0, d1, 0.2.symbol)
                    //val d by opSubtraction(d1, d0)
                    //val d by opSmoothSubtraction(d1, d0, 0.2.symbol)
                    val b by if_(d lt 0.0) { (1.0).symbol } else_ { 0.0.symbol }
                    imageMemoryBarrier {
                        p_sdf.store(c_giid.int, b)
                    }
                }
            }

            val filterSdf = extractBoxes(vt, instances, acb)
            val material = Material()
            material.color = ColorRGBa.PINK.toLinear().toVector4()
            material.roughness = 0.2
            material.metallic = 0.3
            val ss = shadeStyle {
                vertexTransform {
                    val worldToGrid by function<Vector3, IntVector3> { world ->
                        round((world + Vector3(1.0)) * Vector3(voxelSize.toDouble()) * 0.5).int
                    }
                    val b_instances by buffer(instances, BufferAccess.READ)
                    val giid by worldToGrid(b_instances.position[c_instance].xyz / (0.5 * voxelSize))
                    val p_sdf by image<RImage3D>(vt, 0, ImageAccess.READ)

                    val u by giid + IntVector3(0, 1, 0)
                    val d by giid + IntVector3(0, -1, 0)
                    val l by giid + IntVector3(-1, 0, 0)
                    val r by giid + IntVector3(1, 0, 0)
                    val f by giid + IntVector3(0, 0, -1)
                    val b by giid + IntVector3(0, 0, 1)

                    val vu by p_sdf.load(u)
                    val vd by p_sdf.load(d)
                    val vl by p_sdf.load(l)
                    val vr by p_sdf.load(r)
                    val vf by p_sdf.load(f)
                    val vb by p_sdf.load(b)
                    val vc by p_sdf.load(giid)

                    doIf((vu eq 1.0) and (x_normal.y gte 1.0)) {
                        x_position = x_position * 0.0
                    }
                    doIf((vd eq 1.0) and (x_normal.y lte -1.0)) {
                        x_position = x_position * 0.0
                    }
                    doIf((vr eq 1.0) and (x_normal.x gte 1.0)) {
                        x_position = x_position * 0.0
                    }
                    doIf((vl eq 1.0) and (x_normal.x lte -1.0)) {
                        x_position = x_position * 0.0
                    }
                    doIf((vf eq 1.0) and (x_normal.z lte -1.0)) {
                        x_position = x_position * 0.0
                    }
                    doIf((vb eq 1.0) and (x_normal.z gte 1.0)) {
                        x_position = x_position * 0.0
                    }

                    x_position *= b_instances.position[c_instance].w
                    x_position += b_instances.position[c_instance].xyz
                }

                fragmentTransform {
                    var o_albedo by fragmentOutput<Vector4>()
                    var o_normal by fragmentOutput<Vector3>()
                    var o_worldPosition by fragmentOutput<Vector3>()
                    var o_viewZ by fragmentOutput<Double>()

                    o_albedo = Vector4(1.0, 1.0, 1.0, 1.0).symbol
                    o_normal = v_worldNormal.normalized
                    o_worldPosition = v_worldPosition
                    o_viewZ = v_viewPosition.z
                }
            }

            val aoFilter = voxelAO(vt, 2)
            val composeFilter = composeBuffers()
            val ao = colorBuffer(width, height, format = ColorFormat.R, type = ColorType.FLOAT16)
            val aoSmoother = aoSmoother()
            val smoothAo = colorBuffer(width, height, format = ColorFormat.R, type = ColorType.FLOAT16)
            val smoothIrr = colorBuffer(width, height, format = ColorFormat.R, type = ColorType.FLOAT16)
            val normalSmoother = normalSmoother()
            val smoothNormals = colorBuffer(width, height, format = ColorFormat.RGB, type = ColorType.FLOAT16)
            val composed = colorBuffer(width, height, format = ColorFormat.RGBa, type = ColorType.FLOAT16)

            val sphereMesh = sphereMesh(radius = 180.0)

            val lightTarget = renderTarget(width, height) {
                colorBuffer(format = ColorFormat.RGBa, type = ColorType.FLOAT16)
                depthBuffer(format = DepthFormat.STENCIL8)
            }

            val lightStyle = shadeStyle {
                fragmentTransform {
                    val p_sdfs by images<RImage3D>(arrayOf(vt, vt, vt), arrayOf(0, 1, 2), BufferAccess.READ)

                    val a by p_sdfs[0].size()
                    val p_radius by parameter<Double>()
                    val p_worldPositions by parameter<Sampler2D>()
                    val p_worldNormals by parameter<Sampler2D>()
                    val p_lightPosition by parameter<Vector3>()
                    val p_cameraPosition by parameter<Vector3>()
                    val uvc by c_screenPosition / p_worldPositions.size(0).double
                    val uv by Vector2(uvc.x, 1.0 - uvc.y)
                    setSeed((uv * 443902.0).uint)

                    val worldNormal by p_worldNormals[uv].xyz
                    val worldPosition by p_worldPositions[uv].xyz

                    val N by worldNormal
                    val V by (p_cameraPosition - worldPosition).normalized



                    val ggxNormalDistribution by function<Double, Double, Double> { NdotH, roughness ->
                        val a2 = roughness * roughness
                        val d = ((NdotH * a2 - NdotH) * NdotH + 1)
                        a2 / (d * d * Math.PI)
                    }

                    val i by variable(a.x)
                    var sum by variable(0.0)



                    i.for_(0 until 16) {
                        //setSeed((worldPosition*18795.3).uint)
                        val lightPosition by p_lightPosition + uniformSphere() * (p_radius)

                        val L by (p_lightPosition - worldPosition).normalized
                        val H by (V+L).normalized
                        val NoH by saturate(N.dot(H))


                        val toLight by (lightPosition - worldPosition).normalized
                        val lambert by saturate(toLight.dot(worldNormal.normalized))

                        doIf(lambert gt 0.0) {
                            val distance by (lightPosition - worldPosition).length
                            //val intensity =
                            val attenuation = 2.0 * ggxNormalDistribution(NoH, 1.0.symbol)
                            val occ by voxelMarch(
                                p_sdfs,
                                worldPosition / 64.0,
                                toLight,
                                distance / 64.0,
                                20
                            ) * attenuation
                            sum += occ
                        }


                    }
                    sum /= 16.0
                    x_fill = Vector4(sum, sum, sum, 1.0)


                }
                parameter("lightPosition", Vector3(30.0*cos(seconds), 50.0 * cos(seconds*0.43), 30.0*sin(seconds)))
                parameter("worldNormals", rt.colorBuffer(1))
                parameter("worldPositions", rt.colorBuffer(2))
                image("sdfs",arrayOf(vt, vt, vt), arrayOf(0, 1, 2))
            }


            val box = boxMesh()
            extend(Orbital())

            extend {
                drawer.clear(ColorRGBa.WHITE)
                acb.reset()

                fillSdf.parameter("radius", 0.3*cos(seconds)+0.3)
                fillSdf.parameter("dent", Vector3(cos(seconds * 0.3), cos(seconds * 0.231), cos(seconds * 0.251)))
                fillSdf.execute(voxelSize, voxelSize, voxelSize)
                filterSdf.execute(voxelSize, voxelSize, voxelSize)

                downscale.image("input", vt, 0)
                downscale.image("output", vt, 1)
                downscale.execute(voxelSize / 2, voxelSize / 2, voxelSize / 2)

                downscale.image("input", vt, 1)
                downscale.image("output", vt, 2)
                downscale.execute(voxelSize / 4, voxelSize / 4, voxelSize / 4)

                val c = acb.read()[0]


                rt.clearDepth(1.0)
                rt.clearColor(0, ColorRGBa.WHITE) // albedo
                rt.clearColor(1, ColorRGBa.BLACK) // normal
                rt.clearColor(2, ColorRGBa.WHITE) // world
                rt.clearColor(3, ColorRGBa.WHITE) // material
                rt.clearColor(4, ColorRGBa(1000.0, 1000.0, 1000.0, 1000.0)) // view z

                drawer.isolatedWithTarget(rt) {
                    //drawer.clear(ColorRGBa.BLACK)
                    ss.suppressDefaultOutput = true

                    ss.output("albedo", ShadeStyleOutput(0, ColorFormat.RGBa, ColorType.UINT8))
                    ss.output("normal", ShadeStyleOutput(1, ColorFormat.RGB, ColorType.FLOAT16))
                    ss.output("worldPosition", ShadeStyleOutput(2, ColorFormat.RGB, ColorType.FLOAT32))
                    ss.output("viewZ", ShadeStyleOutput(4, ColorFormat.R, ColorType.FLOAT32))
                    drawer.shadeStyle = ss

                    val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                    drawer.shadeStyle?.parameter("cameraPosition", cameraPosition)

                    drawer.cullTestPass = CullTestPass.FRONT
                    drawer.vertexBufferInstances(listOf(box), emptyList(), DrawPrimitive.TRIANGLES, c)
                }
                drawer.isolatedWithTarget(lightTarget) {

                    drawer.clear(ColorRGBa.TRANSPARENT)
                    println(lightStyle)
                    drawer.shadeStyle = lightStyle
                    lightStyle.image("sdfs",arrayOf(vt, vt, vt), arrayOf(0, 1, 2))
                    lightStyle.parameter("radius", (100.0*mouse.position.x)/width)
                    lightStyle.parameter("lightPosition", Vector3(30.0*cos(seconds), 50.0 * cos(seconds*0.43), 30.0*sin(seconds)))
                    val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                    lightStyle.parameter("cameraPosition", cameraPosition)

                    drawer.translate(Vector3(30.0*cos(seconds), 50.0 * cos(seconds*0.43), 30.0*sin(seconds)))
                    drawer.vertexBuffer(sphereMesh, DrawPrimitive.TRIANGLES)


                }


                drawer.defaults()

                //normalSmoother.apply(rt.colorBuffer(1), smoothNormals)

                aoFilter.apply(arrayOf(rt.colorBuffer(1), rt.colorBuffer(2)), arrayOf(ao))
                aoSmoother.apply(arrayOf(ao, rt.colorBuffer(4), rt.colorBuffer(1)), smoothAo)
                aoSmoother.apply(arrayOf(lightTarget.colorBuffer(0), rt.colorBuffer(4), rt.colorBuffer(1)), smoothIrr)

                composeFilter.apply(
                    arrayOf(
                        rt.colorBuffer(0), // albedo
                        rt.colorBuffer(0), // material
                        smoothAo, // ambOcc,
                        rt.colorBuffer(1), // world normal
                        smoothIrr // irradiance
                    ),
                    arrayOf(composed)
                )
                drawer.image(composed)
                //drawer.image(lightTarget.colorBuffer(0))
            }
        }
    }
}