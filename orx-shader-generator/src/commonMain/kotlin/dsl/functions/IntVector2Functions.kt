package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntVector2Functions {

    @JvmName("absSiv2")
    fun abs(x: Symbol<IntVector2>): Symbol<IntVector2> = functionSymbol(x, "abs($0)")

    @JvmName("divVdSv2")
    operator fun Double.div(right: Symbol<IntVector2>): Symbol<Vector2> = functionSymbol(this, right, "($0 / $1")


    @JvmName("timesSiv2Vd")
    operator fun Symbol<IntVector2>.times(right: Double): Symbol<Vector2> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiv2Sd")
    operator fun Symbol<IntVector2>.times(right: Symbol<Double>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 * $1)")


    val Symbol<IntVector2>.x: Symbol<Int>
        @JvmName("xSiv2")
        get() = functionSymbol(this, "$0.x")
    val Symbol<IntVector2>.y: Symbol<Int>
        @JvmName("ySiv2")
        get() = functionSymbol(this, "$0.y")
    val Symbol<IntVector2>.yx: Symbol<IntVector2>
        @JvmName("yxSiv2")
        get() = functionSymbol(this, "$0.yx")
    val Symbol<IntVector2>.xx: Symbol<IntVector2>
        @JvmName("xxSiv2")
        get() = functionSymbol(this, "$0.xx")
}