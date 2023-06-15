package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.phrases.dsl.Image2D
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Image2DFunctions : Generator{
    @JvmName("loadSi2Siv2")
    fun Symbol<Image2D>.load(uv: Symbol<IntVector2>): Symbol<Vector4> =
        functionSymbol(this, uv, "imageLoad($0, $1)")

    @JvmName("storeSi2Siv2Sv4")
    fun Symbol<Image2D>.store(uv: Symbol<IntVector2>, data: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, uv,  data,"imageStore($0, $1, $2)")

    @JvmName("storeSi2Siv2Scrgba")
    fun Symbol<Image2D>.store(uv: Symbol<IntVector2>, data: Symbol<ColorRGBa>): Symbol<Vector4> =
        functionSymbol(this, uv, data, "imageStore($0, $1, $2)")

    @JvmName("sizeSi2")
    fun Symbol<Image2D>.size(): Symbol<IntVector2> =
        functionSymbol(this, "imageSize($0)")

}