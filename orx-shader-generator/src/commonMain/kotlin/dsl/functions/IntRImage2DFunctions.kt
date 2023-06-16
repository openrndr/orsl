package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.Image2D
import org.openrndr.extra.shadergenerator.dsl.IntImage2D
import org.openrndr.math.IntVector2
import org.openrndr.math.IntVector4
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntRImage2DFunctions : Generator{
    @JvmName("loadSiri2Siv2")
    fun Symbol<IntImage2D>.load(uv: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv, "imageLoad($0, $1)")

    @JvmName("storeSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.store(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"imageStore($0, $1, $2)")

    @JvmName("sizeSiri2")
    fun Symbol<IntImage2D>.size(): Symbol<IntVector2> =
        functionSymbol(this, "imageSize($0)")

    @JvmName("atomicExchangeSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicExchange(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicExchange($0, $1, $2)")

    @JvmName("atomicCompSwapSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicCompSwap(uv: Symbol<IntVector2>, compare: Symbol<Int>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv, compare,  data,"atomicExchange($0, $1, $2, $3)")

    @JvmName("atomicAddSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicAdd(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, $2)")

    @JvmName("atomicSubSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicSub(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, -($2))")

    @JvmName("atomicAndSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicAnd(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAnd($0, $1, $2)")

    @JvmName("atomicOrSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicOr(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicOr($0, $1, $2)")

    @JvmName("atomicXorSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicXor(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicXor($0, $1, $2)")

    @JvmName("atomicMinSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicMin(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMin($0, $1, $2)")

    @JvmName("atomicMaxSiri2Siv2Sv4")
    fun Symbol<IntImage2D>.atomicMax(uv: Symbol<IntVector2>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMax($0, $1, $2)")
}