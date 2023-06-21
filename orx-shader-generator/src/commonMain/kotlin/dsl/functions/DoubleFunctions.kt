package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.IntVector2
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface DoubleFunctions {

    @JvmName("saturateSd")
    fun saturate(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "clamp($0, 0.0, 1.0)")

    @JvmName("absSd")
    fun abs(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "abs($0)")

    @JvmName("acosSd")
    fun acos(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "acos($0)")

    @JvmName("acoshSd")
    fun acosh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "acosh($0)")

    @JvmName("asinSd")
    fun asin(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "asin($0)")

    @JvmName("asinhSd")
    fun asinh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "asinh($0)")

    @JvmName("atanSd")
    fun atan(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "atan($0)")

    @JvmName("atanSdSd")
    fun atan(x: Symbol<Double>, y: Symbol<Double>): Symbol<Double> = functionSymbol(x, y, "atan($0, $1)")

    @JvmName("atanhSd")
    fun atanh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "atanh($0)")

    @JvmName("ceilSd")
    fun ceil(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "ceil($0)")

    @JvmName("ceilSdSdSd")
    fun clamp(x: Symbol<Double>, min: Symbol<Double>, max: Symbol<Double>): Symbol<Double> =
        functionSymbol(x, min, max, "clamp($0, $1, $2)")

    @JvmName("cosSd")
    fun cos(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "cos($0)")

    @JvmName("coshSd")
    fun cosh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "cosh($0)")

    @JvmName("degreesSd")
    fun degrees(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "degrees($0)")

    @JvmName("expSd")
    fun exp(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "exp($0)")

    @JvmName("exp2Sd")
    fun exp2(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "exp2($0)")

    @JvmName("fmaSdSdSd")
    fun fma(a: Symbol<Double>, b: Symbol<Double>, c: Symbol<Double>): Symbol<Double> =
        functionSymbol(a, b, c, "fma($0, $1, $2)")

    @JvmName("fractSd")
    fun fract(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "fract($0)")


    @JvmName("floorSd")
    fun floor(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "floor($0)")

    @JvmName("fwidthSd")
    fun fwidth(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "fwidth($0)")

    @JvmName("fwidthCoarseSd")
    fun fwidthCoarse(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "fwidthCoarse($0)")

    @JvmName("fwidthFineSd")
    fun fwidthFine(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "fwidthFine($0)")


    @JvmName("inversesqrtSd")
    fun inversesqrt(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "inversesqrt($0)")

    @JvmName("isinfSd")
    fun isinf(x: Symbol<Double>): Symbol<Boolean> = functionSymbol(x, "isinf($0)")

    @JvmName("isnanSd")
    fun isnan(x: Symbol<Double>): Symbol<Boolean> = functionSymbol(x, "isnan($0)")

    @JvmName("logSd")
    fun log(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "log($0)")

    @JvmName("log2Sd")
    fun log2(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "log2($0)")


    @JvmName("maxSdSd")
    fun max(a: Symbol<Double>, b: Symbol<Double>): Symbol<Double> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("maxSdVd")
    fun max(a: Symbol<Double>, b: Double): Symbol<Double> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("maxVdSd")
    fun max(a: Double, b: Symbol<Double>): Symbol<Double> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("modSdVd")
    fun Symbol<Double>.mod(n: Double): Symbol<Double> = functionSymbol(this, n, "mod($0, $1)")

    @JvmName("modSdVd")
    fun Symbol<Double>.mod(n: Symbol<Double>): Symbol<Double> = functionSymbol(this, n, "mod($0, $1)")

    @JvmName("minSdSd")
    fun min(a: Symbol<Double>, b: Symbol<Double>): Symbol<Double> = functionSymbol(a, b, "min($0, $1)")

    @JvmName("powSdSd")
    fun pow(x: Symbol<Double>, y: Symbol<Double>): Symbol<Double> = functionSymbol(x, y, "pow($0, $1)")

    @JvmName("radiansSd")
    fun radians(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "radians($0)")

    @JvmName("roundSd")
    fun round(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "round($0)")

    @JvmName("roundEvenSd")
    fun roundEven(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "roundEven($0)")

    @JvmName("signSd")
    fun sign(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sign($0)")


    @JvmName("sinSd")
    fun sin(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sin($0)")

    @JvmName("sinhSd")
    fun sinh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sinh($0)")


    @JvmName("smoothstepSdSdSd")
    fun smoothstep(edge0: Symbol<Double>, edge1: Symbol<Double>, x: Symbol<Double>): Symbol<Double> =
        functionSymbol(edge0, edge1, x, "smoothstep($0, $1, $2)")

    @JvmName("smoothstepVdVdSd")
    fun smoothstep(edge0: Double, edge1: Double, x: Symbol<Double>): Symbol<Double> =
        functionSymbol(edge0, edge1, x, "smoothstep($0, $1, $2)")

    @JvmName("sqrtSd")
    fun sqrt(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sqrt($0)")

    @JvmName("stepSdSd")
    fun step(edge: Symbol<Double>, x: Symbol<Double>): Symbol<Double> = functionSymbol(edge, x, "step($0)")

    @JvmName("tanSd")
    fun tan(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "tan($0)")

    @JvmName("tanhSd")
    fun tanh(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "tanh($0)")

    @JvmName("trunSd")
    fun trunc(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "trunc($0)")

    operator fun Symbol<Double>.unaryMinus(): Symbol<Double> =
        functionSymbol(this, "(-$0)")

    @JvmName("plusSdSd")
    operator fun Symbol<Double>.plus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusVdSd")
    operator fun Double.plus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSdSi")
    operator fun Symbol<Double>.plus(right: Symbol<Int>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSdVd")
    operator fun Symbol<Double>.plus(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSdVi")
    operator fun Symbol<Double>.plus(right: Int): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusVdSd")
    operator fun Double.minus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusSdSd")
    operator fun Symbol<Double>.minus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusSdVd")
    operator fun Symbol<Double>.minus(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("timesSdSd")
    operator fun Symbol<Double>.times(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesVdSd")
    operator fun Double.times(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSdVd")
    operator fun Symbol<Double>.times(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSdVi")
    operator fun Symbol<Double>.times(right: Int): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("divSdSd")
    operator fun Symbol<Double>.div(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSdVd")
    operator fun Symbol<Double>.div(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divVdSd")
    operator fun Double.div(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")


    @JvmName("gteSdSd")
    infix fun Symbol<Double>.gte(right: Symbol<Double>): Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gteSdVd")
    infix fun Symbol<Double>.gte(right: Double): Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")


    @JvmName("gtSdSd")
    infix fun Symbol<Double>.gt(right: Symbol<Double>): Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("gtSdVd")
    infix fun Symbol<Double>.gt(right: Double): Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("ltSdSd")
    infix fun Symbol<Double>.lt(right: Symbol<Double>): Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("ltSdVd")
    infix fun Symbol<Double>.lt(right: Double): Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("lteSdSd")
    infix fun Symbol<Double>.lte(right: Symbol<Double>): Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    @JvmName("lteSdVd")
    infix fun Symbol<Double>.lte(right: Double): Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    @JvmName("mixSdSdSd")
    fun Symbol<Double>.mix(right: Symbol<Double>, factor: Symbol<Double>): Symbol<Double> =
        functionSymbol(this, right, factor, "mix($0, $1, $2)")

    val Symbol<Double>.int: Symbol<Int>
        @JvmName("intSd")
        get() = functionSymbol(this, "int($0)")


    val Symbol<Double>.uint: Symbol<UInt>
        @JvmName("uintSd")
        get() = functionSymbol(this, "uint($0)")

}

val Double.symbol: Symbol<Double>
    get() {
        return symbol<Double>(glsl(this)!!)
    }