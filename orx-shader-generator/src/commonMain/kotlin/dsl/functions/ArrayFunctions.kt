package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.*
import kotlin.math.max
import kotlin.math.min

interface ArrayFunctions : Generator {
    operator fun <T> ArraySymbol<T>.get(index: Int): Symbol<T> {
        return object : Symbol<T> {
            override val name = "${this@get.name}[$index]"
        }
    }

    fun ArraySymbol<Double>.max(): Symbol<Double> {
        emitPreamble("#pragma import floatArrayMax($length)")
        return symbol("floatArrayMax_$length(${this@max.name})")
    }

    fun <T> ArraySymbol<T>.drop(n: Int) : ArraySymbol<T> {
        val hash = hash(type, length, n)
        val newLength = max(0, length - n)
        val offset = length - newLength
        emitPreamble("""$type[$newLength] drop_$hash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $newLength; ++i) {
        result[i] = x[$offset + i];
    }
    return result;
}""")
        return arraySymbol("drop_$hash(${this@drop.name})", length, type)
    }

    fun <T> ArraySymbol<T>.dropLast(n: Int) : ArraySymbol<T> {
        val hash = hash(type, length, n)
        val newLength = max(0, length - n)
        emitPreamble("""$type[$newLength] drop_$hash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $newLength; ++i) {
        result[i] = x[i];
    }
    return result;
}""")
        return arraySymbol("drop_$hash(${name})", newLength, type)
    }

    fun <T> ArraySymbol<T>.take(n: Int) : ArraySymbol<T> {
        val hash = hash(type, length, n)
        val newLength = min(length, n)
        emitPreamble("""$type[$newLength] take_$hash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $n; ++i) {
        result[i] = x[i];
    }
    return result;
}""")
        return arraySymbol("take_$hash(${name})", newLength, type)
    }

    fun <T> ArraySymbol<T>.takeLast(n: Int) : ArraySymbol<T> {
        val hash = hash(type, length, n)
        val newLength = min(length, n)
        val offset = length - newLength
        emitPreamble("""$type[$newLength] take_$hash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $n; ++i) {
        result[i] = x[$offset + i];
    }
    return result;
}""")
        return arraySymbol("take_$hash(${this@takeLast.name})", newLength, type)
    }

    fun ArraySymbol<Double>.min(): Symbol<Double> {
        emitPreamble("#pragma import floatArrayMin($length)")
        return symbol("floatArrayMin_$length(${this@min.name})")
    }

    fun ArraySymbol<Double>.sum(): Symbol<Double> {
        emitPreamble("#pragma import floatArraySum($length)")
        return symbol("floatArraySum_$length(${this@sum.name})")
    }

    operator fun <T> ArraySymbol<T>.set(index: Int, value: Symbol<T>): Symbol<T> {
        emit("${this@set.name}[$index] = ${value.name};")
        return symbol("${this@set.name}[$index] = ${value.name}")
    }

    operator fun <T> ArraySymbol<T>.set(index: Int, value: T) {
        emit("${this@set.name}[$index] = ${glsl(value)};")
    }
}