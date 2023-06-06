package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.glsl
import org.openrndr.extra.shadergenerator.dsl.symbol
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

    @JvmName("divSiSd")
    operator fun Symbol<Int>.div(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSiVd")
    operator fun Symbol<Int>.div(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")



    @JvmName("eqSiSi")
    infix fun Symbol<Int>.eq(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("eqSiVi")
    infix fun Symbol<Int>.eq(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("neqSiSi")
    infix fun Symbol<Int>.neq(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("neqSiVi")
    infix fun Symbol<Int>.neq(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("gteSiSi")
    infix fun Symbol<Int>.gte(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gteSiVi")
    infix fun Symbol<Int>.gte(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gtSiSi")
    infix fun Symbol<Int>.gt(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("gtSiVi")
    infix fun Symbol<Int>.gt(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("ltSiSi")
    infix fun Symbol<Int>.lt(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("ltSiVi")
    infix fun Symbol<Int>.lt(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("lteSiSi")
    infix fun Symbol<Int>.lte(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    @JvmName("lteSiVi")
    infix fun Symbol<Int>.lte(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")
}

val Int.symbol: Symbol<Int>
    get() = symbol(glsl(this)!!)