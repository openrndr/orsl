package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.symbol
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@JvmName("fbmSd")
fun ShaderBuilder.fbm(
    function: (x: Symbol<Double>) -> FunctionSymbol1<Double, Double>,
    octaves: Int = 3,
    gain: Double = 0.5,
    lacunarity: Double = 2.0,
): Functions.FunctionPropertyProvider<Double, Double> {
    return Functions.FunctionPropertyProvider(this@fbm, "float", "float") {
        emit("""float sum_ = 0.0;
float lx_ = x__;
float amplitude_ = 1.0;
for (int i_ = 0; i_ < ${octaves}; ++i_) {     
    sum_ += amplitude * ${function(symbol("lx_")).name};
    lx_ *= $lacunarity;
    amplitude_ *= $gain;
}""".trimMargin())
        symbol("sum_")
    }
}

@JvmName("billowSd")
fun ShaderBuilder.billow(
    function: (x: Symbol<Double>) -> FunctionSymbol1<Double, Double>,
    octaves: Int = 3,
    gain: Double = 0.5,
    lacunarity: Double = 2.0,
): Functions.FunctionPropertyProvider<Double, Double> {
    return Functions.FunctionPropertyProvider(this@billow, "float", "float") {
        emit("""float sum_ = 0.0;
float lx_ = x__;
float amplitude_ = 1.0;
for (int i_ = 0; i_ < ${octaves}; ++i_) {     
    sum_ += amplitude_ * abs(${function(symbol("lx_")).name} * 2.0 + 1.0);
    lx_ *= $lacunarity;
    amplitude_ *= $gain;
}""".trimMargin())
        symbol("sum_")
    }
}


@JvmName("fbmSv2")
fun ShaderBuilder.fbm(
    function: (x: Symbol<Vector2>) -> FunctionSymbol1<Vector2, Double>,
    octaves: Int = 3,
    gain: Double = 0.5,
    lacunarity: Double = 2.0,
): Functions.FunctionPropertyProvider<Vector2, Double> {
    return Functions.FunctionPropertyProvider(this@fbm, "vec2", "float") {
        emit("""float sum_ = 0.0;
vec2 lx_ = x__;
float amplitude_ = 1.0;
for (int i_ = 0; i_ < ${octaves}; ++i_) {     
    sum_ += amplitude_ * ${function(symbol("lx_")).name};
    lx_ *= $lacunarity;
    amplitude_ *= $gain;
}""".trimMargin())
        symbol("sum_")
    }
}

@JvmName("fbmSv3")
fun ShaderBuilder.fbm(
    function: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    octaves: Int = 3,
    gain: Double = 0.5,
    lacunarity: Double = 2.0,
): Functions.FunctionPropertyProvider<Vector3, Double> {
    return Functions.FunctionPropertyProvider(this@fbm, "vec3", "float") {
        emit("""float sum_ = 0.0;
vec3 lx_ = x__;
float amplitude_ = 1.0;
for (int i_ = 0; i_ < ${octaves}; ++i_) {     
    sum_ += amplitude_ * ${function(symbol("lx_")).name};
    lx_ *= $lacunarity;
    amplitude_ *= $gain;
}""".trimMargin())
        symbol("sum_")
    }
}

@JvmName("fbmSv4")
fun ShaderBuilder.fbm(
    function: (x: Symbol<Vector4>) -> FunctionSymbol1<Vector4, Double>,
    octaves: Int = 3,
    gain: Double = 0.5,
    lacunarity: Double = 2.0,
): Functions.FunctionPropertyProvider<Vector4, Double> {
    return Functions.FunctionPropertyProvider(this@fbm, "vec4", "float") {
        emit("""float sum_ = 0.0;
vec4 lx_ = x__;
float amplitude_ = 1.0;
for (int i_ = 0; i_ < ${octaves}; ++i_) {     
    sum_ += amplitude_ * ${function(symbol("lx_")).name};
    lx_ *= $lacunarity;
    amplitude_ *= $gain;
}""".trimMargin())
        symbol("sum_")
    }
}
