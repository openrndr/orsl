package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Vector2Functions {
    @JvmName("cosSv2")
    fun cos(x: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(x, "cos($0)")

    @JvmName("sinSv2")
    fun sin(x: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(x, "sin($0)")

    @JvmName("absSv2")
    fun abs(x: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(x, "abs($0)")

    @JvmName("sqrtSv2")
    fun sqrt(x: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(x, "sqrt($0)")

    @JvmName("powSv2Sv2")
    fun pow(x: Symbol<Vector2>, y: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(x, y, "pow($0, $1)")

    @JvmName("dotSv2Sv2")
    fun Symbol<Vector2>.dot(right: Symbol<Vector2>): Symbol<Vector2> = functionSymbol(this, right, "dot($0, $1)")


    @JvmName("mixSv2Sv2Sb")
    fun Symbol<Vector2>.mix(right: Symbol<Vector2>, factor: Symbol<Boolean>): Symbol<Vector2> = functionSymbol(this, right, factor, "mix($0, $1, ($2) ? 1.0 : 0.0)")

    @JvmName("mixSv2Sv2d")
    fun Symbol<Vector2>.mix(right: Symbol<Vector2>, factor: Symbol<Double>): Symbol<Vector2> = functionSymbol(this, right, factor, "mix($0, $1, $2)")



    @JvmName("plusSv2Sv2")
    operator fun Symbol<Vector2>.plus(right: Symbol<Vector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSv2Vv2")
    operator fun Symbol<Vector2>.plus(right: Vector2): Symbol<Vector2> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSv2Sv2")
    operator fun Symbol<Vector2>.minus(right: Symbol<Vector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusSv2Vv2")
    operator fun Symbol<Vector2>.minus(right: Vector2): Symbol<Vector2> = functionSymbol(this, right, "($0 - $1)")


    @JvmName("timesSv2Vd")
    operator fun Symbol<Vector2>.times(right: Double): Symbol<Vector2> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv2Sd")
    operator fun Symbol<Vector2>.times(right: Symbol<Double>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv2Sv2")
    operator fun Symbol<Vector2>.times(right: Symbol<Vector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv2Siv2")
    operator fun Symbol<Vector2>.times(right: Symbol<IntVector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 * $1)")


    @JvmName("divSv2Sd")
    operator fun Symbol<Vector2>.div(right: Symbol<Double>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSv2Vd")
    operator fun Symbol<Vector2>.div(right: Double): Symbol<Vector2> =
        functionSymbol(this, right, "($0 / $1)")



    @JvmName("divSdSv2")
    operator fun Symbol<Double>.div(right: Symbol<Vector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 / $1)")


    @JvmName("divVdSv2")
    operator fun Double.div(right: Symbol<Vector2>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 / $1)")


    @JvmName("timesSv2Vv2")
    operator fun Symbol<Vector2>.times(right: Vector2): Symbol<Vector2> = functionSymbol(this, right, "($0 * $1)")

    val Symbol<Vector2>.length: Symbol<Double>
        @JvmName("lengthSv2")
        get() = functionSymbol(this, "length($0)")
    val Symbol<Vector2>.normalized: Symbol<Vector2>
        @JvmName("normalizedSv2")
        get() = functionSymbol(this, "normalize($0)")
    val Symbol<Vector2>.x: Symbol<Double>
        @JvmName("xSv2")
        get() = functionSymbol(this, "$0.x")
    val Symbol<Vector2>.y: Symbol<Double>
        @JvmName("ySv2")
        get() = functionSymbol(this, "$0.y")
    val Symbol<Vector2>.yx: Symbol<Vector2>
        @JvmName("yxSv2")
        get() = functionSymbol(this, "$0.yx")
    val Symbol<Vector2>.xx: Symbol<Vector2>
        @JvmName("xxSv2")
        get() = functionSymbol(this, "$0.xx")

    @Suppress("FunctionName")
    @JvmName("vec2SdSd")
    fun Vector2(x: Symbol<Double>, y: Symbol<Double>): Symbol<Vector2> =
        functionSymbol(x, y, "vec2($0, $1)")

    val Symbol<Vector2>.int: Symbol<IntVector2>
        @JvmName("intSv2")
        get() = functionSymbol(this, "ivec2($0)")

    val Symbol<Vector2>.uint: Symbol<UIntVector2>
        @JvmName("uintSv2")
        get() = functionSymbol(this, "uvec2($0)")



}

val Vector2.symbol: Symbol<Vector2>
    get() {
        return symbol<Vector2>(glsl(this)!!)
    }