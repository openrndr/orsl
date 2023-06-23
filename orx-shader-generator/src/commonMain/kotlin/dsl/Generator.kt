package org.openrndr.extra.shadergenerator.dsl


val generatorStack = ArrayDeque<Generator>()
interface Generator {
    fun emit(code: String) {
        emit("", code)
    }

    fun emit(symbol: String, code: String) {
        if (code.isNotBlank()) {
            if (symbol.isBlank() || symbol !in declaredSymbols) {
                this.code += code.trimEnd() + "\n"
                if (symbol.isNotBlank()) {
                    declaredSymbols.add(symbol)
                }
            }
        }
    }
    fun emitPreamble(code: String) {
        emitPreamble("", code)
    }

    fun emitPreamble(symbol: String, code: String) {
        if (code.isNotBlank()) {
            if (symbol.isBlank() || symbol !in declaredSymbols) {
                this.preamble += code.trimEnd() + "\n"
                if (symbol.isNotBlank()) {
                    declaredSymbols.add(symbol)
                }
            }
        }
    }


    fun push() {
        generatorStack.addLast(this)
    }

    fun pop() {
        generatorStack.removeLast()
    }

    val declaredSymbols: MutableSet<String>
    var code: String
    var preamble: String
}

fun activeGenerator():Generator = generatorStack.lastOrNull() ?: error("no active generator")