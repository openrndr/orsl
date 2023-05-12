package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol2
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.functionSymbol
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntFunctions {

    @JvmName("absSi")
    fun abs(x: Symbol<Int>): Symbol<Int> = functionSymbol(x, "abs($0)")

    @JvmName("minSiSi")
    fun min(a: Symbol<Int>, b: Symbol<Int>): Symbol<Int> = functionSymbol(a, b, "min($0, $1)")

    @JvmName("maxSisi")
    fun max(a: Symbol<Int>, b: Symbol<Int>): Symbol<Int> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("unaryMinusSi")
    operator fun Symbol<Int>.unaryMinus(): Symbol<Int> =
        functionSymbol(this, "(-$0)")

    @JvmName("plusSiSi")
    operator fun Symbol<Int>.plus(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiSd")
    operator fun Symbol<Int>.plus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiVi")
    operator fun Symbol<Int>.plus(right: Int): Symbol<Int> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiVd")
    operator fun Symbol<Int>.plus(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSiSi")
    operator fun Symbol<Int>.minus(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("timesSiSi")
    operator fun Symbol<Int>.times(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiVi")
    operator fun Symbol<Int>.times(right: Int): Symbol<Int> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiVd")
    operator fun Symbol<Int>.times(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("divSiSi")
    operator fun Symbol<Int>.div(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 / $1)")


}