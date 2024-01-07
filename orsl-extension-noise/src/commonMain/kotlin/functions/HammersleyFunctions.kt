package org.openrndr.orsl.extension.noise.functions
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.math.Vector2

fun ShaderBuilder.hammersley2D(i: Symbol<Int>, N: Symbol<Int>): Symbol<Vector2> {

    //http://holger.dammertz.org/stuff/notes_HammersleyOnHemisphere.html

    val radicalInverse by function<UInt, Double> { bits ->
        var b by variable(bits)
        b = (b shl 16u) or (b shr 16u)
        b = ((b and 0x55555555u) shl 1u) or ((bits and 0xAAAAAAAAu) shr 1u)
        b = ((b and 0x33333333u) shl 2u) or ((b and 0xCCCCCCCCu) shr 2u)
        b = ((b and 0x0F0F0F0Fu) shl 4u) or ((b and 0xF0F0F0F0u) shr 4u)
        b = ((b and 0x00FF00FFu) shl 8u) or ((b and 0xFF00FF00u) shr 8u)
        b * 2.3283064365386963e-10
    }

    val hammersley2D by function<Int, Int, Vector2> { i, N ->
        Vector2(i.double / N.double, radicalInverse(i.uint))
    }

    return hammersley2D(i, N)
}