package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.functionSymbol
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Vector3Functions {
    @JvmName("cosSv3")
    fun cos(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "cos($0)")

    @JvmName("sinSv3")
    fun sin(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "sin($0)")

    @JvmName("absSv3")
    fun abs(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "abs($0)")

    @JvmName("sqrtSv3")
    fun sqrt(x: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, "sqrt($0)")

    @JvmName("powSv3Sv3")
    fun pow(x: Symbol<Vector3>, y: Symbol<Vector3>): Symbol<Vector3> = functionSymbol(x, y, "pow($0, $1)")

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
        @JvmName("ySv3")
        get() = functionSymbol(this, "$0.y")
    val Symbol<Vector3>.xy: Symbol<Vector2>
        @JvmName("xySv3")
        get() = functionSymbol(this, "$0.xy")
    val Symbol<Vector3>.yz: Symbol<Vector2>
        @JvmName("yzSv3")
        get() = functionSymbol(this, "$0.yz")

    @JvmName("vec3Sv2Sd")
    fun Vector3(xy: Symbol<Vector2>, z: Symbol<Double>) : Symbol<Vector3> =
        functionSymbol(xy, z, "vec3($0, $1)")

    @JvmName("vec3SdSv2")
    fun Vector3(x: Symbol<Double>, yz: Symbol<Vector2>) : Symbol<Vector3> =
        functionSymbol(x, yz, "vec3($0, $1)")


}
