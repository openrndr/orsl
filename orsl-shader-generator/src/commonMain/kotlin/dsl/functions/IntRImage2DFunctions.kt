package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.math.IntVector2
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntRImage2DFunctions : Generator{
    @JvmName("loadSiri2Siv2")
    fun Symbol<IntRImage2D>.load(uv: Symbol<IntVector2>): Symbol<Int> =
        functionSymbol(this, uv, "imageLoad($0, $1)")

    @JvmName("storeSiri2Siv2Sv4")
    fun Symbol<IntRImage2D>.store(uv: Symbol<IntVector2>, data: Symbol<Int>) =
        functionCall(this, uv,  data,"imageStore($0, $1, ivec4($2))")

    @JvmName("sizeSiri2")
    fun Symbol<IntRImage2D>.size(): Symbol<IntVector2> =
        functionSymbol(this, "imageSize($0)")

    @JvmName("atomicExchangeSiri2Siv2Sv4")
    fun Symbol<IntRImage2D>.atomicExchange(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicExchange($0, $1, $2)")

    @JvmName("atomicCompSwapSiri2Siv2Sv4")
    fun Symbol<IntRImage2D>.atomicCompSwap(uv: Symbol<IntVector2>, compare: Symbol<Int>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv, compare,  data,"atomicExchange($0, $1, $2, $3)")

    @JvmName("atomicAddSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicAdd(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, $2)")

    @JvmName("atomicAddSiri2Siv2Vi")
    fun Symbol<IntRImage2D>.atomicAdd(uv: Symbol<IntVector2>, data: Int): Symbol<Int> =
        functionSymbolSSV(this, uv,  data, "imageAtomicAdd($0, $1, $2)")


    @JvmName("atomicSubSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicSub(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, -($2))")

    @JvmName("atomicAndSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicAnd(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAnd($0, $1, $2)")

    @JvmName("atomicOrSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicOr(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicOr($0, $1, $2)")

    @JvmName("atomicXorSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicXor(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicXor($0, $1, $2)")

    @JvmName("atomicMinSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicMin(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMin($0, $1, $2)")

    @JvmName("atomicMaxSiri2Siv2Si")
    fun Symbol<IntRImage2D>.atomicMax(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMax($0, $1, $2)")
}