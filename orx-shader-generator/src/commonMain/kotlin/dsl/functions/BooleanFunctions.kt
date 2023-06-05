package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import kotlin.jvm.JvmName


@Suppress("INAPPLICABLE_JVM_NAME")
interface BooleanFunctions : Generator{

    @JvmName("andSbVb")
    infix fun Symbol<Boolean>.and(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 && $1)")

    @JvmName("andSbSb")
    infix fun Symbol<Boolean>.and(right:Symbol<Boolean>) : Symbol<Boolean> = functionSymbol(this, right, "($0 && $1)")

    @JvmName("orSbVb")
    infix fun Symbol<Boolean>.or(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 || $1)")

    @JvmName("orSbSb")
    infix fun Symbol<Boolean>.or(right:Symbol<Boolean>) : Symbol<Boolean> = functionSymbol(this, right, "($0 || $1)")


    @JvmName("xorSbSb")
    infix fun Symbol<Boolean>.xor(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 ^^ $1)")

    @JvmName("notSb")
    fun not(right:Boolean) : Symbol<Boolean> = functionSymbol(this, "!($0)")


    @JvmName("eqSbVb")
    infix fun Symbol<Boolean>.eq(right:Boolean) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("eqSbSb")
    infix fun Symbol<Boolean>.eq(right:Symbol<Boolean>) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")


}

val Boolean.symbol: Symbol<Boolean>
    get() {
        return symbol<Boolean>(glsl(this)!!)
    }