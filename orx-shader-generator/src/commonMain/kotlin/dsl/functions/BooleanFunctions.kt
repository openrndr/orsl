package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import kotlin.jvm.JvmName


@Suppress("INAPPLICABLE_JVM_NAME")
interface BooleanFunctions : Generator{

    @JvmName("andSbSb")
    infix fun Symbol<Boolean>.and(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 && $1)")
    @JvmName("orSbVb")
    infix fun Symbol<Boolean>.or(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 || $1)")

    @JvmName("orSbSb")
    infix fun Symbol<Boolean>.or(right:Symbol<Boolean>) : Symbol<Boolean> = functionSymbol(this, right, "($0 || $1)")


    @JvmName("xorSbSb")
    infix fun Symbol<Boolean>.xor(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 ^^ $1)")

    @JvmName("notSb")
    fun not(right:Boolean) : Symbol<Boolean> = functionSymbol(this, "!($0)")
}