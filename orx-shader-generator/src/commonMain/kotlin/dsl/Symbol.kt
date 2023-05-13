package org.openrndr.extra.shadergenerator.phrases.dsl

import org.openrndr.extra.shadergenerator.phrases.dsl.structs.Struct
import org.openrndr.extra.shadergenerator.phrases.dsl.structs.StructSymbol
import org.openrndr.math.Vector4
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

interface Symbol<T> {
    val name: String
    val type: String
}


fun <T> symbol(name: String, type: String) = object : Symbol<T> {
    override val name = name
    override val type = type
}

inline fun <reified T> symbol(name: String) = object : Symbol<T> {
    override val name = name
    override val type = staticType<T>()
}


interface ArraySymbol<T> {
    val name: String
    val length: Int
    val type: String
}

fun <T> arraySymbol(name: String, length: Int, type: String) = object : ArraySymbol<T> {
    override val name = name
    override val length = length
    override val type = type
}

inline fun <reified T> arraySymbol(name: String, length: Int) = object : ArraySymbol<T> {
    override val name = name
    override val length = length
    override val type = staticType<T>()
}

data class FunctionSymbol1<T0, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            return function.replace("$0", x0)
        }
}

inline fun <reified T0, reified R> functionSymbol(v0: T0, function: String): Symbol<R> =
    FunctionSymbol1<T0, R>(v0 = v0, function = function, type = staticType<R>())

inline fun <reified T0, reified R> functionSymbol(p0: Symbol<T0>, function: String): Symbol<R> =
    FunctionSymbol1<T0, R>(p0 = p0, function = function, type = staticType<R>())


data class FunctionSymbol2<T0, T1, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val function: String,
    override val type: String

) : Symbol<R> {

    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1)
        }
}

inline fun <reified T0, reified T1, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    function: String
): Symbol<R> =
    FunctionSymbol2(p0 = p0, p1 = p1, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified R> functionSymbol(p0: Symbol<T0>, v1: T1, function: String): Symbol<R> =
    FunctionSymbol2(p0 = p0, v1 = v1, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified R> functionSymbol(v0: T0, p1: Symbol<T1>, function: String): Symbol<R> =
    FunctionSymbol2(v0 = v0, p1 = p1, function = function, type = staticType<R>())


data class FunctionSymbol3<T0, T1, T2, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val v2: T2? = null,
    val p2: Symbol<T2>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            val x2 = p2?.name ?: glsl(v2) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1).replace("$2", x2)
        }
}

inline fun <reified T0, reified T1, reified T2, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    p2: Symbol<T2>,
    function: String
): Symbol<R> =
    FunctionSymbol3(p0 = p0, p1 = p1, p2 = p2, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified T2, reified R> functionSymbol(
    v0: T0,
    v1: T1,
    p2: Symbol<T2>,
    function: String
): Symbol<R> =
    FunctionSymbol3(v0 = v0, v1 = v1, p2 = p2, function = function, type = staticType<R>())


data class StructFunctionSymbol1<T0:Struct<T0>, R>(
    val v0: T0? = null,
    val p0: StructSymbol<T0>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            return function.replace("$0", x0)
        }
}
