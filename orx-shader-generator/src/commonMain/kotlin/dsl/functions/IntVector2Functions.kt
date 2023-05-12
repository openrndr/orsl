package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol2
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.functionSymbol
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
    operator fun Symbol<IntVector2>.times(right: Symbol<Double>): Symbol<Vector2> = functionSymbol(this, right, "($0 * $1)")

}