package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface DoubleFunctions {

    @JvmName("saturateSd")
    fun saturate(x: Symbol<Double>):Symbol<Double> = functionSymbol(x, "clamp($0, 0.0, 1.0)")

    @JvmName("cosSd")
    fun cos(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "cos($0)")

    @JvmName("sinSd")
    fun sin(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "sin($0)")

    @JvmName("absSd")
    fun abs(x: Symbol<Double>): Symbol<Double> = functionSymbol(x, "abs($0)")

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


}