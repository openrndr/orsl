package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.math.IntVector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntRImage3DFunctions : Generator{
    @JvmName("loadSiri3Siv3")
    fun Symbol<IntRImage3D>.load(uvw: Symbol<IntVector3>): Symbol<Int> =
        functionSymbol(this, uvw, "imageLoad($0, $1).x")

    @JvmName("storeSiri3Siv3i")
    fun Symbol<IntRImage3D>.store(uvw: Symbol<IntVector3>, data: Symbol<Int>) {
        functionCall(this, uvw, data, "imageStore($0, $1, ivec4($2))")
    }

    @JvmName("sizeSiri3")
    fun Symbol<IntRImage3D>.size(): Symbol<IntVector3> =
        functionSymbol(this, "imageSize($0)")



    @JvmName("atomicExchangeSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicExchange(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicExchange($0, $1, $2)")

    @JvmName("atomicCompSwapSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicCompSwap(uv: Symbol<IntVector3>, compare: Symbol<Int>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv, compare,  data,"atomicExchange($0, $1, $2, $3)")

    @JvmName("atomicAddSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicAdd(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, $2)")

    @JvmName("atomicAddSiri3Siv3Vi")
    fun Symbol<IntRImage3D>.atomicAdd(uv: Symbol<IntVector3>, data: Int): Symbol<Int> =
        functionSymbolSSV(this, uv,  data, "imageAtomicAdd($0, $1, $2)")


    @JvmName("atomicSubSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicSub(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAdd($0, $1, -($2))")

    @JvmName("atomicAndSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicAnd(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicAnd($0, $1, $2)")

    @JvmName("atomicOrSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicOr(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicOr($0, $1, $2)")

    @JvmName("atomicXorSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicXor(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicXor($0, $1, $2)")

    @JvmName("atomicMinSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicMin(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMin($0, $1, $2)")

    @JvmName("atomicMaxSiri3Siv3Si")
    fun Symbol<IntRImage3D>.atomicMax(uv: Symbol<IntVector3>, data: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, uv,  data,"atomicMax($0, $1, $2)")

}