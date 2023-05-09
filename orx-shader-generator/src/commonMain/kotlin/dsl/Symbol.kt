package org.openrndr.extra.shadergenerator.phrases.dsl

import org.openrndr.math.Vector4
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

interface Symbol<T> {
    val name: String
}

interface ArraySymbol<T> {
    val name: String
    val length: Int
    val type: String
}

data class FunctionSymbol1<T0, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val function: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            return function.replace("$0", x0)
        }
}

data class FunctionSymbol2<T0, T1, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val function: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1)
        }
}

data class FunctionSymbol3<T0, T1, T2, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val v2: T2? = null,
    val p2: Symbol<T2>? = null,
    val function: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            val x2 = p2?.name ?: glsl(v2) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1).replace("$2", x2)
        }
}


fun Vector4.symbol(): Symbol<Vector4> {
    return object : Symbol<Vector4> {
        override val name: String = "vec4(${this@symbol.x},${this@symbol.y},${this@symbol.z},${this@symbol.w})"
    }
}