package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbolSVV
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Vector4Functions {
    @JvmName("cosSv4")
    fun cos(x: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(x, "cos($0)")

    @JvmName("sinSv4")
    fun sin(x: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(x, "sin($0)")

    @JvmName("absSv4")
    fun abs(x: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(x, "abs($0)")

    @JvmName("sqrtSv4")
    fun sqrt(x: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(x, "sqrt($0)")

    @JvmName("powSv4Sv4")
    fun pow(x: Symbol<Vector4>, y: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(x, y, "pow($0, $1)")

    @JvmName("mixSv4Sv4Sb")
    fun Symbol<Vector4>.mix(right: Symbol<Vector4>, factor: Symbol<Boolean>): Symbol<Vector4> = functionSymbol(this, right, factor, "mix($0, $1, ($2) ? 1.0 : 0.0)")

    @JvmName("mixSv4Sv4Sd")
    fun Symbol<Vector4>.mix(right: Symbol<Vector4>, factor: Symbol<Double>): Symbol<Vector4> = functionSymbol(this, right, factor, "mix($0, $1, $2)")


    @JvmName("plusSv4Sv4")
    operator fun Symbol<Vector4>.plus(right: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSv4Vv4")
    operator fun Symbol<Vector4>.plus(right: Vector4): Symbol<Vector4> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSv4Sv4")
    operator fun Symbol<Vector4>.minus(right: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusSv4Vv4")
    operator fun Symbol<Vector4>.minus(right: Vector4): Symbol<Vector4> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("timesSv4Vd")
    operator fun Symbol<Vector4>.times(right: Double): Symbol<Vector4> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesVdSv4")
    operator fun Double.times(right: Symbol<Vector4>): Symbol<Vector4> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSdSv4")
    operator fun Symbol<Double>.times(right: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 * $1)")


    @JvmName("timesSv4Sd")
    operator fun Symbol<Vector4>.times(right: Symbol<Double>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv4Sv4")
    operator fun Symbol<Vector4>.times(right: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv4Vv4")
    operator fun Symbol<Vector4>.times(right: Vector4): Symbol<Vector4> = functionSymbol(this, right, "($0 * $1)")


    @JvmName("divSv4Vd")
    operator fun Symbol<Vector4>.div(right: Double): Symbol<Vector4> = functionSymbol(this, right, "($0 / $1)")



    val Symbol<Vector4>.length: Symbol<Double>
        @JvmName("lengthSv4")
        get() = functionSymbol(this, "length($0)")
    val Symbol<Vector4>.normalized: Symbol<Vector4>
        @JvmName("normalizedSv4")
        get() = functionSymbol(this, "normalize($0)")
    val Symbol<Vector4>.x: Symbol<Double>
        @JvmName("xSv4")
        get() = functionSymbol(this, "$0.x")
    val Symbol<Vector4>.y: Symbol<Double>
        @JvmName("ySv4")
        get() = functionSymbol(this, "$0.y")

    val Symbol<Vector4>.z: Symbol<Double>
        @JvmName("zSv4")
        get() = functionSymbol(this, "$0.z")

    val Symbol<Vector4>.w: Symbol<Double>
        @JvmName("wSv4")
        get() = functionSymbol(this, "$0.w")
    val Symbol<Vector4>.xy: Symbol<Vector2>
        @JvmName("xySv4")
        get() = functionSymbol(this, "$0.xy")
    val Symbol<Vector4>.yz: Symbol<Vector2>
        @JvmName("yzSv4")
        get() = functionSymbol(this, "$0.yz")

    val Symbol<Vector4>.xyz: Symbol<Vector3>
        @JvmName("xyzSv4")
        get() = functionSymbol(this, "$0.xyz")

    val Symbol<Vector4>.yzw: Symbol<Vector3>
        @JvmName("yzwSv4")
        get() = functionSymbol(this, "$0.xyz")



    @Suppress("FunctionName")
    @JvmName("vec4Sv2Sv2")
    fun Vector4(xy: Symbol<Vector2>, zw: Symbol<Vector2>) : Symbol<Vector4> =
        functionSymbol(xy, zw, "vec4($0, $1)")


    @Suppress("FunctionName")
    @JvmName("vec4SdSdSdSd")
    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Symbol<Double>) : Symbol<Vector4> =
        functionSymbol(x, y, z, w, "vec4($0, $1, $2, $3)")


    @Suppress("FunctionName")
    @JvmName("vec4SdSdSdVd")
    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Double) : Symbol<Vector4> =
        functionSymbol(x, y, z, w, "vec4($0, $1, $2, $3)")


    @Suppress("FunctionName")
    @JvmName("vec4Sv2Sd")
    fun Vector4(xyz: Symbol<Vector3>, w: Symbol<Double>) : Symbol<Vector4> =
        functionSymbol(xyz, w, "vec4($0, $1)")

    @Suppress("FunctionName")
    @JvmName("vec4Sv3Vd")
    fun Vector4(xyz: Symbol<Vector3>, w: Double) : Symbol<Vector4> =
        functionSymbol(xyz, w, "vec4($0, $1)")

    @Suppress("FunctionName")
    @JvmName("vec4Sv2VdVd")
    fun Vector4(xy: Symbol<Vector2>, z: Double, w: Double) : Symbol<Vector4> =
        functionSymbolSVV(xy, z, w, "vec4($0, $1, $2)")
}
