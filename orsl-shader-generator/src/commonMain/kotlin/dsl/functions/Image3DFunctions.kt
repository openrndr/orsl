package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.Image2D
import org.openrndr.math.IntVector2
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Image3DFunctions : Generator{
    @JvmName("loadSi3Siv3")
    fun Symbol<Image3D>.load(uvw: Symbol<IntVector3>): Symbol<Vector4> =
        functionSymbol(this, uvw, "imageLoad($0, $1)")

    @JvmName("storeSi3Siv3Sv4")
    fun Symbol<Image3D>.store(uvw: Symbol<IntVector3>, data: Symbol<Vector4>) {
        functionCall(this, uvw, data, "imageStore($0, $1, $2)")
    }

    @JvmName("storeSi3Siv3Scrgba")
    fun Symbol<Image3D>.store(uv: Symbol<IntVector3>, data: Symbol<ColorRGBa>): Symbol<Vector4> =
        functionSymbol(this, uv, data, "imageStore($0, $1, $2)")

    @JvmName("sizeSi3")
    fun Symbol<Image3D>.size(): Symbol<IntVector3> =
        functionSymbol(this, "imageSize($0)")

}