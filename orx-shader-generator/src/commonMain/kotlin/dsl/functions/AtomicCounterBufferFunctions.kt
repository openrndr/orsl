package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.draw.AtomicCounterBuffer
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface AtomicCounterBufferFunctions : Generator {

    @JvmName("getSAcbVi")
    operator fun Symbol<AtomicCounterBuffer>.get(index: Int) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicCounter($0[$1])")
    }

    @JvmName("getSAcbSi")
    operator fun Symbol<AtomicCounterBuffer>.get(index: Symbol<Int>) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicCounter($0[$1])")
    }

    @JvmName("incrementSAcbVi")
    fun Symbol<AtomicCounterBuffer>.increment(index: Int) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicIncrement($0[$1])")
    }

    @JvmName("incrementSAcbSi")
    fun Symbol<AtomicCounterBuffer>.increment(index: Symbol<Int>) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicIncrement($0[$1])")
    }

    @JvmName("decrementSAcbVi")
    fun Symbol<AtomicCounterBuffer>.decrement(index: Int) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicDecrement($0[$1])")
    }

    @JvmName("decrementSAcbSi")
    fun Symbol<AtomicCounterBuffer>.decrement(index: Symbol<Int>) : Symbol<UInt> {
        return functionSymbol(this, index,"atomicDecrement($0[$1])")
    }

}