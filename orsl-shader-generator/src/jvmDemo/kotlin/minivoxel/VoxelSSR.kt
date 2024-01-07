package minivoxel

import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.orsl.shadergenerator.dsl.RImage3D
import org.openrndr.orsl.shadergenerator.dsl.Sampler2D
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.extra.bnHemisphere
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.extra.setBNMask2
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.extra.setSeed
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.extra.uniform2
import org.openrndr.math.*

fun voxelSSR(voxel: VolumeTexture, rayCount: Int = 4, stepsPerRayCount: Int = 20): ShadeStyleFilter1to1 {
    return shadeStyleFilter1to1 {
        fragmentTransform {
            val p_sdfs by images<RImage3D>(arrayOf(voxel, voxel, voxel), arrayOf(0, 1, 2), BufferAccess.READ)
            val p_cameraPosition by parameter<Vector3>()
            val p_view by parameter<Matrix44>()
            val p_viewProjection by parameter<Matrix44>()

            var g_voxelIntersection by global<Vector3>()
            var g_voxelIntersectionFound by global<Boolean>()

            val v_texCoord0 by parameter<Vector2>()

            val tex0 by parameter<Sampler2D>() // worldNormal
            val tex1 by parameter<Sampler2D>() // worldPosition
            val tex2 by parameter<Sampler2D>() // viewZ
            val tex3 by parameter<Sampler2D>() // radiance
            val radiance = tex3

            val tex4 by parameter<Sampler2D>() // blue noise map

            val tex5 by parameter<Sampler2D>() // material
            val material by tex5[v_texCoord0]

            val roughness by material.z


            val worldNormal by tex0[v_texCoord0].xyz.normalized
            val worldPosition by tex1[v_texCoord0].xyz
            val viewZ by tex2[v_texCoord0].x

            val bnuv by ((v_texCoord0 * tex0.size()) / tex4.size().double).mod(Vector2.ONE.symbol)
            val bnmask by tex4[bnuv].xy
            setBNMask2(bnmask)
            setSeed(4023U.symbol)

            var sum by variable(Vector3(0.0))
            val ray by (worldPosition - p_cameraPosition).normalized

            val gl_FragCoord by parameter<IntVector2>()
            doIf( mod((gl_FragCoord.x + gl_FragCoord.y), 2) eq 0) {



                var i by variable(0)


                i.for_(0 until 1) {
                    val distortedNormal by (bnHemisphere(worldNormal) * roughness * 0.5 + worldNormal).normalized
                    val reflectedRay = ray.reflect(distortedNormal)

                    g_voxelIntersection = Vector3(1.0, 0.0, 0.0).symbol
                    val b by voxelMarch(
                        p_sdfs,
                        worldPosition / (p_sdfs[0].size().double * 0.5),
                        reflectedRay,
                        worldNormal,
                        128.0.symbol,
                        128
                    )
                    val clipPosition by p_viewProjection * Vector4(
                        g_voxelIntersection * (p_sdfs[0].size().double * 0.5),
                        1.0
                    )
                    val screenPosition by clipPosition.xyz / clipPosition.w
                    val screenUV by (screenPosition.xy + Vector2.ONE) / 2.0
                    val screenPixel by (screenUV * tex0.size()).int
                    val sampleNormal by tex0.fetch(screenPixel, 0).xyz
                    val reflectedViewZ by (p_view * Vector4(g_voxelIntersection * 64.0, 1.0)).z
                    val sampleViewZ by tex2.fetch(screenPixel, 0).x

                    val normalFade by saturate(sampleNormal.normalized.dot(-reflectedRay.normalized))
                    val depthFade by 1.0 - abs((reflectedViewZ - sampleViewZ) * 0.1)


                    val vignette = smoothstep(1.0, 0.5, abs(screenPosition.x)) * smoothstep(
                        1.0,
                        0.5,
                        abs(screenPosition.y)
                    ) * normalFade * depthFade

                    val screenValue by radiance.fetch(screenPixel, 0).xyz * vignette
                    doIf(g_voxelIntersectionFound) {
                        sum = screenValue
                    }

                }
            }
            //sum /= rayCount.toDouble()
            //emit("how in the hell")
            x_fill = Vector4(sum, 1.0)
        }
    }
}