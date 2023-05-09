package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.ArraySymbol
import org.openrndr.extra.shadergenerator.phrases.dsl.Generator
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.glsl
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
        return object : Symbol<Double> {
            override val name = "floatArrayMax_$length(${this@max.name})"
        }
    }

    fun <T> ArraySymbol<T>.drop(n: Int) : ArraySymbol<T> {
        var hash = type.hashCode()
        hash = hash * 31 + length.hashCode()
        hash = hash * 31 + n.hashCode()
        val fhash = hash.toUInt().toString()
        val newLength = max(0, length - n)
        val offset = length - newLength
        emitPreamble("""$type[$newLength] drop_$fhash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $newLength; ++i) {
        result[i] = x[$offset + i];
    }
    return result;
}""")
        return object : ArraySymbol<T> {
            override val name = "drop_$fhash(${this@drop.name})"
            override val length = newLength
            override val type = this@drop.type
        }
    }

    fun <T> ArraySymbol<T>.dropLast(n: Int) : ArraySymbol<T> {
        var hash = type.hashCode()
        hash = hash * 31 + length.hashCode()
        hash = hash * 31 + n.hashCode()
        val fhash = hash.toUInt().toString()
        val newLength = max(0, length - n)
        emitPreamble("""$type[$newLength] drop_$fhash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $newLength; ++i) {
        result[i] = x[i];
    }
    return result;
}""")
        return object : ArraySymbol<T> {
            override val name = "drop_$fhash(${this@dropLast.name})"
            override val length = newLength
            override val type = this@dropLast.type
        }
    }

    fun <T> ArraySymbol<T>.take(n: Int) : ArraySymbol<T> {
        var hash = type.hashCode()
        hash = hash * 31 + length.hashCode()
        hash = hash * 31 + n.hashCode()
        val fhash = hash.toUInt().toString()
        val newLength = min(length, n)
        emitPreamble("""$type[$newLength] take_$fhash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $n; ++i) {
        result[i] = x[i];
    }
    return result;
}""")
        return object : ArraySymbol<T> {
            override val name = "take_$fhash(${this@take.name})"
            override val length = newLength
            override val type = this@take.type
        }
    }

    fun <T> ArraySymbol<T>.takeLast(n: Int) : ArraySymbol<T> {
        var hash = type.hashCode()
        hash = hash * 31 + length.hashCode()
        hash = hash * 31 + n.hashCode()
        val fhash = hash.toUInt().toString()
        val newLength = min(length, n)
        val offset = length - newLength
        emitPreamble("""$type[$newLength] take_$fhash($type x[$length]) {
    $type result[$newLength];
    for (int i = 0; i < $n; ++i) {
        result[i] = x[$offset + i];
    }
    return result;
}""")
        return object : ArraySymbol<T> {
            override val name = "take_$fhash(${this@takeLast.name})"
            override val length = newLength
            override val type = this@takeLast.type
        }
    }




    fun ArraySymbol<Double>.min(): Symbol<Double> {
        emitPreamble("#pragma import floatArrayMin($length)")
        return object : Symbol<Double> {
            override val name = "floatArrayMin_$length(${this@min.name})"
        }
    }

    fun ArraySymbol<Double>.sum(): Symbol<Double> {
        emitPreamble("#pragma import floatArraySum($length)")
        return object : Symbol<Double> {
            override val name = "floatArraySum_$length(${this@sum.name})"
        }
    }

    operator fun <T> ArraySymbol<T>.set(index: Int, value: Symbol<T>): Symbol<T> {
        emit("${this@set.name}[$index] = ${value.name};")
        return object : Symbol<T> {
            override val name = "${this@set.name}[$index] = ${value.name}"
        }
    }

    operator fun <T> ArraySymbol<T>.set(index: Int, value: T): Symbol<T> {
        emit("${this@set.name}[$index] = ${glsl(value)};")
        return object : Symbol<T> {
            override val name = "BORK"
        }
    }


}