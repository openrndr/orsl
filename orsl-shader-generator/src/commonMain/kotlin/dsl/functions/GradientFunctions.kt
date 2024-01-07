package org.openrndr.orsl.shadergenerator.phrases.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.FunctionSymbol1
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.Functions
import org.openrndr.math.*
import kotlin.jvm.JvmName


@JvmName("gradientSd")
fun ShaderBuilder.gradient(
    function: (x: Symbol<Double>) -> FunctionSymbol1<Double, Double>,
    eps: Double = 1E-6,
): Functions.FunctionPropertyProvider<Double, Double> {
    return Functions.FunctionPropertyProvider(true, this@gradient, "float", "float") {
        val dx by eps
        (function(it + dx) - function(it - dx)) / (2.0 * eps)
    }
}

@JvmName("gradientSv2")
fun ShaderBuilder.gradient(
    function: (x: Symbol<Vector2>) -> FunctionSymbol1<Vector2, Double>,
    eps: Double = 1E-6,
    ): Functions.FunctionPropertyProvider<Vector2, Vector2> {
    return Functions.FunctionPropertyProvider(true, this@gradient, "vec2", "vec2") {
        val dx by Vector2(eps, 0.0)
        val dy by Vector2(0.0, eps)

        val dfdx by (function(it + dx) - function(it - dx)) / (2.0 * eps)
        val dfdy by (function(it + dy) - function(it - dy)) / (2.0 * eps)
        Vector2(dfdx, dfdy)
    }
}

@JvmName("gradientSv3")
fun ShaderBuilder.gradient(
    function: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    eps: Double = 1E-6,
    ): Functions.FunctionPropertyProvider<Vector3, Vector3> {
    return Functions.FunctionPropertyProvider(true, this@gradient, "vec3", "vec3") {
        val dx by Vector3(eps, 0.0, 0.0)
        val dy by Vector3(0.0, eps, 0.0)
        val dz by Vector3(0.0, 0.0, eps)

        val dfdx by (function(it + dx) - function(it - dx)) / (2.0 * eps)
        val dfdy by (function(it + dy) - function(it - dy)) / (2.0 * eps)
        val dfdz by (function(it + dz) - function(it - dz)) / (2.0 * eps)
        Vector3(dfdx, dfdy, dfdz)
    }
}

@JvmName("gradientSv4")
fun ShaderBuilder.gradient(
    function: (x: Symbol<Vector4>) -> FunctionSymbol1<Vector4, Double>,
    eps: Double = 1E-6
    ): Functions.FunctionPropertyProvider<Vector4, Vector4> {
    return Functions.FunctionPropertyProvider(true, this@gradient, "vec4", "vec4") {
        val dx by Vector4(eps, 0.0, 0.0, 0.0)
        val dy by Vector4(0.0, eps, 0.0, 0.0)
        val dz by Vector4(0.0, 0.0, eps, 0.0)
        val dw by Vector4(0.0, 0.0, 0.0, eps)

        val dfdx by (function(it + dx) - function(it - dx)) / (2.0 * eps)
        val dfdy by (function(it + dy) - function(it - dy)) / (2.0 * eps)
        val dfdz by (function(it + dx) - function(it - dz)) / (2.0 * eps)
        val dfdw by (function(it + dy) - function(it - dw)) / (2.0 * eps)
        Vector4(dfdx, dfdy, dfdz, dfdw)
    }
}

fun ShaderBuilder.jacobian(
    function: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Vector3>,
    eps: Double = 1E-6
    ): Functions.FunctionPropertyProvider<Vector3, Matrix33> {
    return Functions.FunctionPropertyProvider(true, this@jacobian, "vec3", "mat3") {
        val dx by Vector3(eps, 0.0, 0.0)
        val dy by Vector3(0.0, eps, 0.0)
        val dz by Vector3(0.0, 0.0, eps)

        val dfdx by (function(it + dx) - function(it - dx)) / (2.0 * eps)
        val dfdy by (function(it + dy) - function(it - dy)) / (2.0 * eps)
        val dfdz by (function(it + dz) - function(it - dz)) / (2.0 * eps)
        Matrix33.fromColumnVectors(dfdx, dfdy, dfdz)
    }
}

@JvmName("jacobianSv4Sv4")
fun ShaderBuilder.jacobian(
    function: (x: Symbol<Vector4>) -> FunctionSymbol1<Vector4, Vector4>,
    eps: Double = 1E-6
): Functions.FunctionPropertyProvider<Vector4, Matrix44> {
    return Functions.FunctionPropertyProvider(true, this@jacobian, "vec4", "mat4") {
        val dx by Vector4(eps, 0.0, 0.0, 0.0)
        val dy by Vector4(0.0, eps, 0.0, 0.0)
        val dz by Vector4(0.0, 0.0, eps, 0.0)
        val dw by Vector4(0.0, 0.0, 0.0, eps)

        val dfdx by (function(it + dx) - function(it - dx)) / (2.0 * eps)
        val dfdy by (function(it + dy) - function(it - dy)) / (2.0 * eps)
        val dfdz by (function(it + dz) - function(it - dz)) / (2.0 * eps)
        val dfdw by (function(it + dw) - function(it - dw)) / (2.0 * eps)
        Matrix44.fromColumnVectors(dfdx, dfdy, dfdz, dfdw)
    }
}