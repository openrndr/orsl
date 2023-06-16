package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface FragmentDerivativeFunctions : Generator {

    /** return the partial derivative with respect to x */
    @JvmName("dFdxSd")
    fun Symbol<Double>.dFdx() : Symbol<Double> = functionSymbol(this, "dFdx($0)")

    /** return the partial derivative with respect to y */
    @JvmName("dFdySd")
    fun Symbol<Double>.dFdy() : Symbol<Double> = functionSymbol(this, "dFdy($0)")

    /** return the sum of the absolute value of derivatives in x and y */
    @JvmName("fwidthSd")
    fun Symbol<Double>.fwidth() : Symbol<Double> = functionSymbol(this, "fwidth($0)")

    /** return the partial derivative with respect to x */
    @JvmName("dFdxSv2")
    fun Symbol<Vector2>.dFdx() : Symbol<Vector2> = functionSymbol(this, "dFdx($0)")

    /** return the partial derivative with respect to y */
    @JvmName("dFdySv2")
    fun Symbol<Vector2>.dFdy() : Symbol<Vector2> = functionSymbol(this, "dFdy($0)")

    /** return the sum of the absolute value of derivatives in x and y */
    @JvmName("fwidthSv2")
    fun Symbol<Vector2>.fwidth() : Symbol<Vector2> = functionSymbol(this, "fwidth($0)")


    /** return the partial derivative with respect to x */
    @JvmName("dFdxSv3")
    fun Symbol<Vector3>.dFdx() : Symbol<Vector3> = functionSymbol(this, "dFdx($0)")

    /** return the partial derivative with respect to y */
    @JvmName("dFdySv3")
    fun Symbol<Vector3>.dFdy() : Symbol<Vector3> = functionSymbol(this, "dFdy($0)")

    /** return the sum of the absolute value of derivatives in x and y */
    @JvmName("fwidthSv3")
    fun Symbol<Vector3>.fwidth() : Symbol<Vector3> = functionSymbol(this, "fwidth($0)")

    /** return the partial derivative with respect to x */
    @JvmName("dFdxSv4")
    fun Symbol<Vector4>.dFdx() : Symbol<Vector4> = functionSymbol(this, "dFdx($0)")

    /** return the partial derivative with respect to y */
    @JvmName("dFdySv4")
    fun Symbol<Vector4>.dFdy() : Symbol<Vector4> = functionSymbol(this, "dFdy($0)")

    /** return the sum of the absolute value of derivatives in x and y */
    @JvmName("fwidthSv4")
    fun Symbol<Vector4>.fwidth() : Symbol<Vector4> = functionSymbol(this, "fwidth($0)")

    @JvmName("dFdxScrgba")
    fun Symbol<ColorRGBa>.dFdx() : Symbol<ColorRGBa> = functionSymbol(this, "dFdx($0)")

    /** return the partial derivative with respect to y */
    @JvmName("dFdyScrgba")
    fun Symbol<ColorRGBa>.dFdy() : Symbol<ColorRGBa> = functionSymbol(this, "dFdy($0)")

    /** return the sum of the absolute value of derivatives in x and y */
    @JvmName("fwidthScrgba")
    fun Symbol<ColorRGBa>.fwidth() : Symbol<ColorRGBa> = functionSymbol(this, "fwidth($0)")

}