package org.openrndr.extra.shadergenerator.phrases.dsl

interface Generator {
    fun emit(code: String)
    fun emitPreamble(code: String)

}