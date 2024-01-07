package org.openrndr.orsl.shadergenerator.phrases.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.Generator
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functionSymbol
import org.openrndr.orsl.shadergenerator.dsl.IntImage2D
import org.openrndr.math.IntVector2
import org.openrndr.math.IntVector4
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntImage2DFunctions : Generator{
    @JvmName("loadSii2Siv2")
    fun Symbol<IntImage2D>.load(uv: Symbol<IntVector2>): Symbol<IntVector4> =
        functionSymbol(this, uv, "imageLoad($0, $1)")

    @JvmName("storeSii2Siv2Sv4")
    fun Symbol<IntImage2D>.store(uv: Symbol<IntVector2>, data: Symbol<IntVector4>): Symbol<IntVector4> =
        functionSymbol(this, uv,  data,"imageStore($0, $1, $2)")

    @JvmName("sizeSii2")
    fun Symbol<IntImage2D>.size(): Symbol<IntVector2> =
        functionSymbol(this, "imageSize($0)")
}