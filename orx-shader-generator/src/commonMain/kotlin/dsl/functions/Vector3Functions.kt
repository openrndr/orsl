package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Vector3Functions {

    @JvmName("absSv3")
    fun abs(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "abs($0)")

    @JvmName("floorSv3")
    fun floor(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "floor($0)")

    @JvmName("ceilSv3")
    fun ceil(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "ceil($0)")


    @JvmName("roundSv3")
    fun round(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "round($0)")


    @JvmName("cosSv3")
    fun cos(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "cos($0)")

    @JvmName("crossSv3")
    fun Symbol<Vector3>.cross(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "cross($0, $1)")

    @JvmName("dotSv3Sv3")
    fun Symbol<Vector3>.dot(right: Symbol<Vector3>): Symbol<Double> = functionSymbol(this, right, "dot($0, $1)")

    @JvmName("dotSv3Vv3")
    fun Symbol<Vector3>.dot(right: Vector3): Symbol<Double> = functionSymbol(this, right, "dot($0, $1)")

    @JvmName("reflectSv3Sv3")
    fun Symbol<Vector3>.reflect(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "reflect($0, $1)")

    @JvmName("reflectSv3Vv3")
    fun Symbol<Vector3>.reflect(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "reflect($0, $1)")

    @JvmName("modSv3Sv3")
    fun Symbol<Vector3>.mod(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "mod($0, $1)")

    @JvmName("modSv3Vv3")
    fun Symbol<Vector3>.mod(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "mod($0, $1)")


    @JvmName("mixSv3Sv3Sb")
    fun Symbol<Vector3>.mix(right: Symbol<Vector3>, factor: Symbol<Boolean>): Symbol<Vector3> = functionSymbol(this, right, factor, "mix($0, $1, ($2) ? 1.0 : 0.0)")

    @JvmName("mixSv3Sv3Sd")
    fun Symbol<Vector3>.mix(right: Symbol<Vector3>, factor: Symbol<Double>): Symbol<Vector3> = functionSymbol(this, right, factor, "mix($0, $1, $2)")

    @JvmName("powSv3Sv3")
    fun pow(x: Symbol<Vector3>, y: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, y, "pow($0, $1)")

    @JvmName("sinSv3")
    fun sin(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "sin($0)")

    @JvmName("sqrtSv3")
    fun sqrt(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "sqrt($0)")


    @JvmName("unaryMinusSv3")
    operator fun Symbol<Vector3>.unaryMinus(): Symbol<Vector3> =
        functionSymbol(this, "(-$0)")

    @JvmName("plusSv3Sv3")
    operator fun Symbol<Vector3>.plus(right: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSv3Sd")
    operator fun Symbol<Vector3>.plus(right: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 + $1)")


    @JvmName("plusSv3Vv3")
    operator fun Symbol<Vector3>.plus(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSv3Sv3")
    operator fun Symbol<Vector3>.minus(right: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusSv3Vv3")
    operator fun Symbol<Vector3>.minus(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("minusVv3Sv3")
    operator fun Vector3.minus(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "($0 - $1)")


    @JvmName("timesVv3Sd")
    operator fun Vector3.times(right: Symbol<Double>): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")


    @JvmName("timesSv3Vd")
    operator fun Symbol<Vector3>.times(right: Double): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesVdSv3")
    operator fun Double.times(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSdSv3")
    operator fun Symbol<Double>.times(right: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")


    @JvmName("timesSv3Sd")
    operator fun Symbol<Vector3>.times(right: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv3Sv3")
    operator fun Symbol<Vector3>.times(right: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSv3Vv3")
    operator fun Symbol<Vector3>.times(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")


    @JvmName("divSv3Vd")
    operator fun Symbol<Vector3>.div(right: Double): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSv3Sd")
    operator fun Symbol<Vector3>.div(right: Symbol<Double>): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSv3Sv3")
    operator fun Symbol<Vector3>.div(right: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSv3Vv3")
    operator fun Symbol<Vector3>.div(right: Vector3): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")



    @JvmName("logSv3")
    fun log(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "log($0)")


    val Symbol<Vector3>.length: Symbol<Double>
        @JvmName("lengthSv3")
        get() = functionSymbol(this, "length($0)")
    val Symbol<Vector3>.normalized: Symbol<Vector3>
        @JvmName("normalizedSv3")
        get() = functionSymbol(this, "normalize($0)")
    val Symbol<Vector3>.x: Symbol<Double>
        @JvmName("xSv3")
        get() = functionSymbol(this, "$0.x")
    val Symbol<Vector3>.y: Symbol<Double>
        @JvmName("zSv3")
        get() = functionSymbol(this, "$0.y")
    val Symbol<Vector3>.z: Symbol<Double>
        @JvmName("ySv3")
        get() = functionSymbol(this, "$0.z")

    val Symbol<Vector3>.xy: Symbol<Vector2>
        @JvmName("xySv3")
        get() = functionSymbol(this, "$0.xy")
    val Symbol<Vector3>.yz: Symbol<Vector2>
        @JvmName("yzSv3")
        get() = functionSymbol(this, "$0.yz")

    val Symbol<Vector3>.xz: Symbol<Vector2>
        @JvmName("xzSv3")
        get() = functionSymbol(this, "$0.xz")


    @Suppress("FunctionName")
    @JvmName("vec3Sv2Sd")
    fun Vector3(xy: Symbol<Vector2>, z: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(xy, z, "vec3($0, $1)")

    @Suppress("FunctionName")
    @JvmName("vec3Sv2Vd")
    fun Vector3(xy: Symbol<Vector2>, z: Double): Symbol<Vector3> =
        functionSymbol(xy, z, "vec3($0, $1)")


    @Suppress("FunctionName")
    @JvmName("vec3SdSv2")
    fun Vector3(x: Symbol<Double>, yz: Symbol<Vector2>): Symbol<Vector3> =
        functionSymbol(x, yz, "vec3($0, $1)")

    @Suppress("FunctionName")
    @JvmName("vec3SdSdSd")
    fun Vector3(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(x, y, z, "vec3($0, $1, $2)")

    val Symbol<Vector3>.int: Symbol<IntVector3>
        @JvmName("intSv3")
        get() = functionSymbol(this, "ivec3($0)")

    val Symbol<Vector3>.uint: Symbol<UIntVector3>
        @JvmName("uintSv3")
        get() = functionSymbol(this, "uvec3($0)")


}

val Vector3.symbol: Symbol<Vector3>
    get() {
        return symbol<Vector3>(glsl(this)!!)
    }