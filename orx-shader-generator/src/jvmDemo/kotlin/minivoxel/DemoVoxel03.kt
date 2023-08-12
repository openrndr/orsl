package minivoxel

import Voxel128
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
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
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.*
import org.openrndr.math.*
import position
import voxelSize
import kotlin.math.cos
import kotlin.math.sin


fun main() {
    application {
        configure {
            width = 640
            height = 640
        }
        program {
            val bn1map = loadImage("data/textures/LDR_LLL1_0.png")
            val bn2map = loadImage("data/textures/LDR_RG01_0.png")


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

                    val p_time by parameter<Double>()

                    val position by 2.0 * (c_giid.double / Vector3(voxelSize.toDouble()) - Vector3(0.5))

                    val xposition by erot(position, Vector3.UNIT_Y.symbol, position.y*4.0 + p_time)

                    //val d0 by sdSphere(position, p_radius)
                    val d0 by -sdBox(position, Vector3(0.9, 0.9, 0.9).symbol)

                    val s by 0.25
                    val gridIndex by floor((xposition + Vector3(s, s, s)) / (2.0 * s))
                    val radius by cos(p_time + gridIndex.x + gridIndex.y + gridIndex.z) * s *0.25 + s * 0.25
                    val d1 by sdSphere(
                        (xposition + Vector3(s, s, s)).mod(
                            Vector3(
                                2.0 * s,
                                2.0 * s,
                                2.0 * s
                            )
                        ) - Vector3(s, s, s), radius
                    )
                    val d by min(d0, d1)

                    //val d0 by -sdSphere(position, 0.99.symbol)
//                    val d1 by sdSphere(position + Vector3(0.0, -0.3, 0.0), p_radius)
//                    val d2 by sdSphere(position + Vector3(0.0, 0.3, 0.0), p_radius)
                    //val d by opSmoothUnion(d0, opSmoothUnion(d1, d2, 0.3.symbol), 0.3.symbol)
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
                    var o_material by fragmentOutput<Vector4>()
                    var o_viewZ by fragmentOutput<Double>()

                    o_albedo = Vector4(1.0, 1.0, 1.0, 1.0).symbol
                    o_normal = v_worldNormal.normalized
                    o_worldPosition = v_worldPosition

                    val bd by sdBox(o_worldPosition, Vector3(55.0, 55.0, 55.0).symbol)

                    val metallic by if_(bd gte 0.0) { smoothstep(0.0, 0.01, value13(v_worldPosition*0.05)) * 0.5 } else_ { 0.0.symbol }
                    val roughness by if_(bd gte 0.0) { 1.0- metallic*2.0 } else_ { 1.0.symbol }
                    val reflective by metallic
                    val emissive by 0.0

                    o_material = Vector4(reflective, metallic, roughness, emissive)
                    o_viewZ = v_viewPosition.z
                }
            }

            val aoFilter = voxelAO(vt, 1, 5)
            val ssrFiler = voxelSSR(vt, 1)
            val composeRadiance = composeRadiance()
            val composeFinal = composeFinal()

            val ao = colorBuffer(width, height, format = ColorFormat.R, type = ColorType.FLOAT16)
            val specular = colorBuffer(width, height, format = ColorFormat.RGB)
            val aoSmoother = aoSmoother(32)
            val smoothAo = colorBuffer(width, height, format = ColorFormat.R, type = ColorType.FLOAT16)
            val smoothIrr = colorBuffer(width, height, format = ColorFormat.RG, type = ColorType.FLOAT16)
            val normalSmoother = normalSmoother()
            val smoothNormals = colorBuffer(width, height, format = ColorFormat.RGB, type = ColorType.FLOAT16)
            val radiance = colorBuffer(width, height, format = ColorFormat.RGBa, type = ColorType.FLOAT16)
            val final = colorBuffer(width, height, format = ColorFormat.RGBa, type = ColorType.FLOAT16)


            val lightMesh = sphereMesh(radius = 5.0)

            val sphereMesh = sphereMesh(radius = 180.0)


            val lightTarget = renderTarget(width, height) {
                colorBuffer(format = ColorFormat.RG, type = ColorType.FLOAT16)
                depthBuffer(format = DepthFormat.STENCIL8)
            }

            val lightSourceStyle = shadeStyle {
                fragmentTransform {
                    var o_albedo by fragmentOutput<Vector4>()
                    var o_normal by fragmentOutput<Vector3>()
                    var o_worldPosition by fragmentOutput<Vector3>()
                    var o_material by fragmentOutput<Vector4>()
                    var o_viewZ by fragmentOutput<Double>()

                    o_albedo = Vector4(1.0, 1.0, 1.0, 1.0).symbol
                    o_normal = v_worldNormal.normalized
                    o_worldPosition = v_worldPosition

                    val metallic by 0.0 //cos(v_worldPosition.x * 0.1) * 0.5 + 0.5
                    val roughness by 0.0
                    val reflective by 0.0
                    val emissive by 1.0

                    o_material = Vector4(reflective, metallic, roughness, emissive)
                    o_viewZ = v_viewPosition.z
                }

            }


            val lightStyle = shadeStyle {
                fragmentTransform {
                    val p_bnmap by parameter<Sampler2D>()
                    val p_sdfs by images<RImage3D>(arrayOf(vt, vt, vt), arrayOf(0, 1, 2), BufferAccess.READ)
                    val a by p_sdfs[0].size()
                    val p_radius by parameter<Double>()
                    val p_worldPositions by parameter<Sampler2D>()
                    val p_worldNormals by parameter<Sampler2D>()
                    val p_material by parameter<Sampler2D>()
                    val p_lightPosition by parameter<Vector3>()
                    val p_cameraPosition by parameter<Vector3>()
                    val uvc by c_screenPosition / p_worldPositions.size(0).double
                    val uv by Vector2(uvc.x, 1.0 - uvc.y)
                    val bnuv = ((uv * p_worldNormals.size()) / p_bnmap.size().double).mod(Vector2.ONE.symbol)
                    val bnmask = p_bnmap[bnuv].xy
                    setBNMask2(bnmask)

                    setSeed(443902U.symbol)

                    val worldNormal by p_worldNormals[uv].xyz.normalized
                    val worldPosition by p_worldPositions[uv].xyz
                    val worldMaterial by p_material[uv]
                    val roughness by worldMaterial.z

                    val N by worldNormal
                    val V by (p_cameraPosition - worldPosition).normalized


                    val i by variable(a.x)
                    var sum by variable(Vector2(0.0, 0.0)) // diffuse, specular

                    val gl_FragCoord by parameter<IntVector2>()
                    doIf(mod((gl_FragCoord.x + gl_FragCoord.y), 2) eq 0) {
                        i.for_(0 until 2) {
                            val lightPosition by p_lightPosition + (bnSphere() * p_radius)

                            val L by (p_lightPosition - worldPosition).normalized
                            val H by (V + L).normalized
                            val NoH by saturate(N.dot(H))
                            val nOv by saturate(N.dot(V)) + 1E-5
                            val nOl by saturate(N.dot(L))
                            val lOh by saturate(H.dot(L))

                            val toLight by (lightPosition - worldPosition).normalized
                            val lambert by saturate(toLight.dot(worldNormal.normalized))

                            doIf(lambert gt 0.0) {
                                val distance by (lightPosition - worldPosition).length
                                val diffuse = 2.0 * Fd_Burley(
                                    roughness * roughness,
                                    nOv,
                                    nOl,
                                    lOh
                                ) * (1.0 / (1.0 + distance * distance * 0.001))


                                val D by D_GGX(roughness * roughness, NoH)
                                val V by V_SmithGGXCorrelated(roughness * roughness, nOv, nOl)
                                val specular by D*V


                                val occ by voxelMarch(
                                    p_sdfs,
                                    worldPosition / 64.0,
                                    toLight,
                                    worldNormal,
                                    distance / 64.0,
                                    128
                                )
                                sum += Vector2(occ * diffuse * lambert, occ * specular * lambert)
                            }
                        }
                        sum /= 2.0
                        sum = max(Vector2(0.0), sum)
                    }
                    x_fill = Vector4(sum.x, sum.y, 0.0.symbol, 1.0)
                }
                parameter(
                    "lightPosition",
                    Vector3(30.0 * cos(seconds), 50.0 * cos(seconds * 0.43), 30.0 * sin(seconds))
                )
                parameter("worldNormals", rt.colorBuffer(1))
                parameter("worldPositions", rt.colorBuffer(2))
                parameter("material", rt.colorBuffer(3))
                parameter("bnmap", bn2map)
                image("sdfs", arrayOf(vt, vt, vt), arrayOf(0, 1, 2))
            }


            val box = boxMesh()
            extend(Orbital()) {
                fov = 15.0
            }

            extend {
                drawer.clear(ColorRGBa.WHITE)
                acb.reset()

                fillSdf.parameter("time", seconds)
                fillSdf.parameter("radius", 0.4)
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
                    ss.output("material", ShadeStyleOutput(3, ColorFormat.RGBa, ColorType.UINT8))
                    ss.output("viewZ", ShadeStyleOutput(4, ColorFormat.R, ColorType.FLOAT32))
                    drawer.shadeStyle = ss

                    val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                    drawer.shadeStyle?.parameter("cameraPosition", cameraPosition)

                    drawer.cullTestPass = CullTestPass.FRONT
                    drawer.vertexBufferInstances(listOf(box), emptyList(), DrawPrimitive.TRIANGLES, c)

                    drawer.shadeStyle = lightSourceStyle
                    lightSourceStyle.output("albedo", ShadeStyleOutput(0, ColorFormat.RGBa, ColorType.UINT8))
                    lightSourceStyle.output("normal", ShadeStyleOutput(1, ColorFormat.RGB, ColorType.FLOAT16))
                    lightSourceStyle.output("worldPosition", ShadeStyleOutput(2, ColorFormat.RGB, ColorType.FLOAT32))
                    lightSourceStyle.output("material", ShadeStyleOutput(3, ColorFormat.RGBa, ColorType.UINT8))
                    lightSourceStyle.output("viewZ", ShadeStyleOutput(4, ColorFormat.R, ColorType.FLOAT32))

                    drawer.translate(Vector3(30.0 * cos(seconds), 50.0 * cos(seconds * 0.43), 30.0 * sin(seconds)))
                    drawer.vertexBuffer(lightMesh, DrawPrimitive.TRIANGLES)
                }
                drawer.isolatedWithTarget(lightTarget) {

                    drawer.clear(ColorRGBa.TRANSPARENT)
                    drawer.drawStyle.cullTestPass = CullTestPass.BACK
                    drawer.shadeStyle = lightStyle
                    lightStyle.image("sdfs", arrayOf(vt, vt, vt), arrayOf(0, 1, 2))
                    lightStyle.parameter("radius", 5.0)
                    lightStyle.parameter(
                        "lightPosition",
                        Vector3(30.0 * cos(seconds), 50.0 * cos(seconds * 0.43), 30.0 * sin(seconds))
                    )
                    val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                    lightStyle.parameter("cameraPosition", cameraPosition)

                    drawer.translate(Vector3(30.0 * cos(seconds), 50.0 * cos(seconds * 0.43), 30.0 * sin(seconds)))
                    drawer.vertexBuffer(sphereMesh, DrawPrimitive.TRIANGLES)
                }

                val v = drawer.view
                val vp = drawer.projection * drawer.view
                drawer.defaults()

                //normalSmoother.apply(rt.colorBuffer(1), smoothNormals)

                aoFilter.parameter("bnmap", bn2map)
                aoFilter.apply(arrayOf(rt.colorBuffer(1), rt.colorBuffer(2)), arrayOf(ao))
                aoSmoother.parameter("bn1map", bn1map)

                aoSmoother.apply(arrayOf(ao, rt.colorBuffer(2), rt.colorBuffer(1)), smoothAo)
                aoSmoother.apply(arrayOf(lightTarget.colorBuffer(0), rt.colorBuffer(2), rt.colorBuffer(1)), smoothIrr)

                composeRadiance.apply(
                    arrayOf(
                        rt.colorBuffer(0), // albedo
                        rt.colorBuffer(3), // material
                        smoothAo, // ambOcc,
                        rt.colorBuffer(1), // world normal
                        smoothIrr // irradiance
                    ),
                    arrayOf(radiance)
                )

                ssrFiler.parameter("viewProjection", vp)
                ssrFiler.parameter("view", v)
                ssrFiler.parameter("cameraPosition", (v.inversed * Vector4.UNIT_W).xyz)
                ssrFiler.apply(
                    arrayOf(
                        rt.colorBuffer(1), // world normal
                        rt.colorBuffer(2), // world position
                        rt.colorBuffer(4), // viewZ
                        radiance, // radiance
                        bn2map,
                        rt.colorBuffer(3)
                    ), specular
                )

                composeFinal.parameter("cameraPosition", (v.inversed * Vector4.UNIT_W).xyz)
                composeFinal.apply(
                    arrayOf(
                        radiance,
                        specular,
                        rt.colorBuffer(1), // world normal
                        rt.colorBuffer(2), // world position
                        rt.colorBuffer(3), // material
                        rt.colorBuffer(0), // albedo
                        smoothAo
                    ), arrayOf(final)
                )
                drawer.image(final)
            }
        }
    }
}