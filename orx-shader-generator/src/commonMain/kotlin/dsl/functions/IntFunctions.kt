package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol2
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntFunctions {

    @JvmName("minusSiSi")
    operator fun Symbol<Int>.minus(right: Symbol<Int>): Symbol<Int> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 - $1)")

    @JvmName("plusSiSi")
    operator fun Symbol<Int>.plus(right: Symbol<Int>): Symbol<Int> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 + $1)")

    @JvmName("timesSiSi")
    operator fun Symbol<Int>.times(right: Symbol<Int>): Symbol<Int> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")

    @JvmName("divSiSi")
    operator fun Symbol<Int>.div(right: Symbol<Int>): Symbol<Int> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 / $1)")



}