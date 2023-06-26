package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.glsl
import org.openrndr.extra.shadergenerator.dsl.symbol
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntVector3unctions {

    @JvmName("absSiv3")
    fun abs(x: Symbol<IntVector3>): Symbol<IntVector3> = functionSymbol(x, "abs($0)")

    @JvmName("divVdSv3")
    operator fun Double.div(right: Symbol<IntVector3>): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")


    @JvmName("timesSiv3Vd")
    operator fun Symbol<IntVector3>.times(right: Double): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiv3Sd")
    operator fun Symbol<IntVector3>.times(right: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("plusSiv3Siv3")
    operator fun Symbol<IntVector3>.plus(right: Symbol<IntVector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiv3Viv3")
    operator fun Symbol<IntVector3>.plus(right: IntVector3): Symbol<IntVector3> =
        functionSymbol(this, right, "($0 + $1)")


    val Symbol<IntVector3>.x: Symbol<Int>
        @JvmName("xSiv3")
        get() = functionSymbol(this, "$0.x")
    val Symbol<IntVector3>.y: Symbol<Int>
        @JvmName("ySiv3")
        get() = functionSymbol(this, "$0.y")

    val Symbol<IntVector3>.z: Symbol<Int>
        @JvmName("zSiv3")
        get() = functionSymbol(this, "$0.z")

    val Symbol<IntVector3>.xy: Symbol<IntVector2>
        @JvmName("xySiv3")
        get() = functionSymbol(this, "$0.xy")
    val Symbol<IntVector3>.yz: Symbol<IntVector2>
        @JvmName("yzSiv3")
        get() = functionSymbol(this, "$0.yz")
    val Symbol<IntVector3>.xz: Symbol<IntVector2>
        @JvmName("xzSiv3")
        get() = functionSymbol(this, "$0.xz")

    @Suppress("FunctionName")
    @JvmName("ivec3SiSiSi")
    fun IntVector3(x: Symbol<Int>, y: Symbol<Int>, z: Symbol<Int>): Symbol<IntVector3> =
        functionSymbol(x, y, z, "ivec3($0, $1, $2)")

}

val IntVector3.symbol: Symbol<IntVector3>
    get() {
        return symbol<IntVector3>(glsl(this)!!)
    }