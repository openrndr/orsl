package minivoxel

import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
fun normalSmoother() = shadeStyleFilter1to1 {

    fragmentTransform {
        val tex0 by parameter<Sampler2D>() // world normals

        val v_texCoord0 by parameter<Vector2>()
        val step by 1.0 / tex0.size()

        val i by variable<Int>()
        val j by variable<Int>()

        var sum by variable(Vector3.ZERO)
        var weight by variable(0.0)

        j.for_(-5 until 6) {
            i.for_(-5 until 6) {
                sum += tex0[v_texCoord0 + step * IntVector2(i, j)].xyz
                weight += 1.0
            }
        }
        x_fill = Vector4(sum.normalized, 1.0)

    }
}