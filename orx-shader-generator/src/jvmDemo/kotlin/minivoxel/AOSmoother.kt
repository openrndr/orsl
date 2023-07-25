package minivoxel

import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.setSeed
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.uniformDisk
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
fun aoSmoother() = shadeStyleFilter1to1 {

    fragmentTransform {
        val tex0 by parameter<Sampler2D>() // ambOcc
        val tex1 by parameter<Sampler2D>() // viewZ
        val tex2 by parameter<Sampler2D>() // worldNormal

        val ambOcc = tex0
        val viewZ = tex1
        val worldNormal = tex2

        val v_texCoord0 by parameter<Vector2>()
        val step by 1.0 / tex0.size()

        val i by variable<Int>()
        val j by variable<Int>()

        var sum by variable(ambOcc[v_texCoord0].x)
        var weight by variable(1.0)


        setSeed( (v_texCoord0*430234.0).uint)

        //uniformDisk()

        val centerNormal by worldNormal[v_texCoord0].xyz.normalized
        val centerZ by viewZ[v_texCoord0].x
            i.for_(0 until 16) {
                val sampleUV = v_texCoord0 + step * uniformDisk() * 8.0
                val sampleZ = viewZ[sampleUV].x

                val sampleAO by ambOcc[sampleUV].x
                val sampleNormal by worldNormal[sampleUV].xyz.normalized

                val d by max(0.0, sampleNormal.dot(centerNormal))
                val sampleWeight by (smoothstep(0.4.symbol, 0.0.symbol, abs(sampleZ - centerZ))) * d

                sum += sampleAO * sampleWeight
                weight += sampleWeight
            }
        sum /= weight
        x_fill = Vector4(sum, sum, sum, 1.0)

    }
}