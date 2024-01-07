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
import org.openrndr.math.IntVector2
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

fun voxelAO(voxel: VolumeTexture, rayCount: Int = 4, stepsPerRayCount: Int = 20): ShadeStyleFilter1to1 {
    return shadeStyleFilter1to1 {
        fragmentTransform {
            val p_sdfs by images<RImage3D>(arrayOf(voxel, voxel, voxel), arrayOf(0, 1, 2), BufferAccess.READ)

            val tex0 by parameter<Sampler2D>()
            val tex1 by parameter<Sampler2D>()

            val p_bnmap by parameter<Sampler2D>()



            val v_texCoord0 by parameter<Vector2>()
            val worldNormal by tex0[v_texCoord0].xyz.normalized
            val worldPosition by tex1[v_texCoord0].xyz / (p_sdfs[0].size().double * 0.5)

            val bnuv = ((v_texCoord0 * tex0.size()) / p_bnmap.size().double).mod(Vector2.ONE.symbol)
            val bnmask = p_bnmap[bnuv].xy
            setBNMask2(bnmask)


            setSeed(4023U.symbol)

            val hemispherePointCos by function<Vector3, Vector3> { normal ->
                val r by uniform2()
                val u by r.x * 2.0 - 1.0
                val v by r.y
                val a by Math.PI * v * 2.0

                val tn by normal + Vector3(Vector2(cos(a), sin(a)) * sqrt(1.0 - u * u), u)
                tn.normalized
            }

            var sum by variable(0.0)

            val gl_FragCoord by parameter<IntVector2>()
            doIf( mod((gl_FragCoord.x + gl_FragCoord.y), 2) eq 0) {

                var i by variable(0)
                i.for_(0 until rayCount) {
                    val direction by bnHemisphere(worldNormal.normalized)
                    //val q by sphericalDistribution(direction, 256.symbol).xyz
                    val ambOcc by voxelMarch(p_sdfs, worldPosition, direction, worldNormal, maxSteps = stepsPerRayCount)
                    sum += ambOcc //* saturate(q.dot(worldNormal.normalized))
                }
                sum /= rayCount.toDouble()
                sum = sum * 0.75 + 0.25
            }
            //emit("how in the hell")
            x_fill = Vector4(sum, sum, sum, 1.0)
        }
    }
}