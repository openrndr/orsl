package org.openrndr.orsl.extension.noise.functions
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.PI

fun ShaderBuilder.hemisphere(uv: Symbol<Vector2>) : Symbol<Vector3> {
    val hemisphere by function<Vector2, Vector3> {
        val phi by uv.y * 2.0 * PI
        val cosTheta by 1.0 - uv.x
        val sinTheta by sqrt(1.0 - cosTheta * cosTheta)
        Vector3(cos(phi) * sinTheta, sin(phi) * sinTheta, cosTheta)
    }
    return hemisphere(uv)
}

fun ShaderBuilder.hemisphereCosine(uv: Symbol<Vector2>) : Symbol<Vector3> {
    val hemisphere by function<Vector2, Vector3> {
        val phi by uv.y * 2.0 * PI
        val cosTheta by sqrt(1.0 - uv.x)
        val sinTheta by sqrt(1.0 - cosTheta * cosTheta)
        Vector3(cos(phi) * sinTheta, sin(phi) * sinTheta, cosTheta)
    }
    return hemisphere(uv)
}