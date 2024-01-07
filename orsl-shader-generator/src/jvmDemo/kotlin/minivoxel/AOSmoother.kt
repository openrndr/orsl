package minivoxel

import org.openrndr.orsl.shadergenerator.dsl.Sampler2D
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.extra.bnDisk
import org.openrndr.math.Vector2

fun aoSmoother(samples: Int) = shadeStyleFilter1to1 {

    fragmentTransform {
        val tex0 by parameter<Sampler2D>() // ambOcc
        val tex1 by parameter<Sampler2D>() // worldPosition
        val tex2 by parameter<Sampler2D>() // worldNormal

        val p_discSamples by arrayParameter<Vector2>(samples)

        val ambOcc = tex0
        val worldPosition = tex1
        val worldNormal = tex2
        val p_bn1map by parameter<Sampler2D>()

        val v_texCoord0 by parameter<Vector2>()
        val step by 1.0 / tex0.size()

        val i by variable<Int>()

        var sum by variable(ambOcc[v_texCoord0].xy)
        var weight by variable(Vector2(1.0, 1.0))

        val bnuv by ((v_texCoord0 * tex0.size()) / p_bn1map.size().double).mod(Vector2.ONE.symbol)

        val discRotation = p_bn1map[bnuv].x * Math.PI * 2.0
        val centerNormal by worldNormal[v_texCoord0].xyz.normalized
        val centerPosition by worldPosition[v_texCoord0].xyz
        val centerAO by ambOcc[v_texCoord0].xy
        i.for_(0 until samples) {
            val s by erot(Vector3(p_discSamples[i], 0.0), org.openrndr.math.Vector3.UNIT_Z.symbol, discRotation).xy
            val sampleUV by v_texCoord0 + step * s * 12.0
            val samplePosition by worldPosition[sampleUV].xyz

            val sampleDirection by (centerPosition - samplePosition)

            val sampleAO by ambOcc[sampleUV].xy
            val sampleNormal by worldNormal[sampleUV].xyz.normalized

            val tangentPlaneDist by abs(sampleDirection.dot(centerNormal))

            val sampleDifference by  max(Vector2(0.0, 0.0), Vector2(1.0, 1.0).symbol - abs(centerAO - sampleAO))

            //            val w by exp(-1.0 * tangentPlaneDist * (1.0)) * max(centerNormal.dot(sampleNormal), 0.0.symbol) //* (1.0 - abs(sampleAO - centerAO))
            val w by sampleDifference * exp(  -1.0  * tangentPlaneDist) * max(centerNormal.dot(sampleNormal), 0.0)
            sum += sampleAO * w
            weight += w
        }
        sum /= weight
        x_fill = Vector4(sum.x, sum.y, 0.0.symbol, 1.0)

    }
    parameter("discSamples", spiralSamples(samples, 11).toTypedArray())
}