package org.openrndr.extra.shadergenerator.phrases.dsl

interface Generator {
    fun emit(code: String)
    fun emitPreamble(code: String)
    fun emitSymbol(symbol: String, f: ()->String)
    fun emitLocalSymbol(symbol: String, f: ()->String)
}