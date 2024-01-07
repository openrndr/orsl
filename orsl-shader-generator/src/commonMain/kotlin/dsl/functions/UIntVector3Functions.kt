package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface UIntVector3Functions {
    @JvmName("divVdSuiv3")
    operator fun Double.div(right: Symbol<UIntVector3>): Symbol<Vector3> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("timesSuiv3Vd")
    operator fun Symbol<UIntVector3>.times(right: Double): Symbol<Vector3> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSuiv3Vui")
    operator fun Symbol<UIntVector3>.times(right: UInt): Symbol<UIntVector3> = functionSymbol(this, right, "($0 * $1u)")

    @JvmName("timesSuiv3Sui")
    operator fun Symbol<UIntVector3>.times(right: Symbol<UInt>): Symbol<UIntVector3> = functionSymbol(this, right, "($0 * $1)")


    @JvmName("timesSuiv3Vi")
    operator fun Symbol<UIntVector3>.times(right: Int): Symbol<IntVector3> = functionSymbol(this, right, "(ivec3($0) * $1)")

    @JvmName("timesSuiv3Si")
    operator fun Symbol<UIntVector3>.times(right: Symbol<Int>): Symbol<IntVector3> = functionSymbol(this, right, "($0 * $1)")


    @JvmName("timesSuiv3Sd")
    operator fun Symbol<UIntVector3>.times(right: Symbol<Double>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")

    val Symbol<UIntVector3>.x: Symbol<UInt>
        @JvmName("xSuiv3")
        get() = functionSymbol(this, "$0.x")
    val Symbol<UIntVector3>.y: Symbol<UInt>
        @JvmName("ySuiv3")
        get() = functionSymbol(this, "$0.y")
    val Symbol<UIntVector3>.z: Symbol<UInt>
        @JvmName("zSuiv3")
        get() = functionSymbol(this, "$0.z")
    val Symbol<UIntVector3>.xy: Symbol<UIntVector2>
        @JvmName("xySuiv3")
        get() = functionSymbol(this, "$0.xy")
    val Symbol<UIntVector3>.yz: Symbol<UIntVector2>
        @JvmName("yzSuiv3")
        get() = functionSymbol(this, "$0.yz")
    val Symbol<UIntVector3>.xz: Symbol<UIntVector2>
        @JvmName("xzSuiv3")
        get() = functionSymbol(this, "$0.xz")

    @Suppress("FunctionName")
    @JvmName("uivec3SuiSuiSui")
    fun UIntVector3(x: Symbol<UInt>, y: Symbol<UInt>, z: Symbol<UInt>): Symbol<UIntVector3> =
        functionSymbol(x, y, z, "ivec3($0, $1, $2)")

    /**
     * cast UIntVector3 to IntVector3
     */
    val Symbol<UIntVector3>.int: Symbol<IntVector3>
        @JvmName("intSuiv3")
        get() = functionSymbol(this, "ivec3($0)")

    /**
     * cast UIntVector3 to Vector3
     */
    val Symbol<UIntVector3>.double: Symbol<Vector3>
        @JvmName("doubleSuiv3")
        get() = functionSymbol(this, "vec3($0)")
}

//val UIntVector3.symbol: Symbol<UIntVector3>
//    get() {
//        return symbol<UIntVector3>(glsl(this)!!)
//    }