package org.openrndr.extra.shadergenerator.dsl


val generatorStack = ArrayDeque<Generator>()
interface Generator {
    fun emit(code: String)
    fun emitPreamble(code: String)


    fun push() {
        generatorStack.addLast(this)
    }

    fun pop() {
        generatorStack.removeLast()
    }
}

fun activeGenerator():Generator = generatorStack.lastOrNull() ?: error("no active generator")