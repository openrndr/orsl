package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface UIntVector2Functions {
    @JvmName("divVdSuiv2")
    operator fun Double.div(right: Symbol<UIntVector2>): Symbol<Vector2> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("timesSuiv2Vd")
    operator fun Symbol<UIntVector2>.times(right: Double): Symbol<Vector2> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSuiv2Sd")
    operator fun Symbol<UIntVector2>.times(right: Symbol<Double>): Symbol<Vector2> =
        functionSymbol(this, right, "($0 * $1)")

    val Symbol<UIntVector2>.x: Symbol<UInt>
        @JvmName("xSuiv2")
        get() = functionSymbol(this, "$0.x")
    val Symbol<UIntVector2>.y: Symbol<UInt>
        @JvmName("ySuiv2")
        get() = functionSymbol(this, "$0.y")

    @Suppress("FunctionName")
    @JvmName("uivec2SuiSui")
    fun UIntVector2(x: Symbol<UInt>, y: Symbol<UInt>): Symbol<UIntVector2> =
        functionSymbol(x, y, "ivec2($0, $1)")

    /**
     * cast UIntVector3 to IntVector3
     */
    val Symbol<UIntVector2>.int: Symbol<IntVector2>
        @JvmName("intSuiv2")
        get() = functionSymbol(this, "ivec2($0)")

    /**
     * cast UIntVector3 to Vector3
     */
    val Symbol<UIntVector2>.double: Symbol<Vector2>
        @JvmName("doubleSuiv2")
        get() = functionSymbol(this, "vec2($0)")
}

//val UIntVector3.symbol: Symbol<UIntVector3>
//    get() {
//        return symbol<UIntVector3>(glsl(this)!!)
//    }