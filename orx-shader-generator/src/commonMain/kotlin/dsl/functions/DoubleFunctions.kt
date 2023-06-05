package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.glsl
import org.openrndr.extra.shadergenerator.dsl.symbol
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface DoubleFunctions {

    @JvmName("saturateSd")
    fun saturate(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "clamp($0, 0.0, 1.0)")

    @JvmName("cosSd")
    fun cos(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "cos($0)")

    @JvmName("sinSd")
    fun sin(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sin($0)")

    @JvmName("absSd")
    fun abs(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "abs($0)")

    @JvmName("floorSd")
    fun floor(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "floor($0)")

    @JvmName("sqrtSd")
    fun sqrt(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sqrt($0)")

    @JvmName("expSd")
    fun exp(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "exp($0)")

    @JvmName("powSdSd")
    fun pow(x: Symbol<Double>, y: Symbol<Double>): Symbol<Double> = functionSymbol(x, y, "pow($0, $1)")

    @JvmName("minSdSd")
    fun min(a: Symbol<Double>, b: Symbol<Double>): Symbol<Double> = functionSymbol(a, b, "min($0, $1)")

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


    @JvmName("smoothstepSdSdSd")
    fun smoothstep(edge0: Symbol<Double>, edge1: Symbol<Double>, x: Symbol<Double>): Symbol<Double> =
        functionSymbol(edge0, edge1, x, "smoothstep($0, $1, $2)")

    @JvmName("smoothstepVdVdSd")
    fun smoothstep(edge0: Double, edge1: Double, x: Symbol<Double>): Symbol<Double> =
        functionSymbol(edge0, edge1, x, "smoothstep($0, $1, $2)")

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
    fun Symbol<Double>.mix(right: Symbol<Double>, factor: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, factor, "mix($0, $1, $2)")
}

val Double.symbol: Symbol<Double>
    get() {
        return symbol<Double>(glsl(this)!!)
    }