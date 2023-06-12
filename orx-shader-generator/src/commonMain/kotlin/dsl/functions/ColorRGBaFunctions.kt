package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName
import kotlin.reflect.KProperty

@Suppress("INAPPLICABLE_JVM_NAME")
interface ColorRGBaFunctions: Generator {

    @JvmName("crgbaSdSdSdSd")
    fun ColorRGBa(
        r: Symbol<Double>,
        g: Symbol<Double>,
        b: Symbol<Double>,
        a: Symbol<Double> = 1.0.symbol
    ): Symbol<ColorRGBa> =
        functionSymbol(r, g, b, a, "vec4($0, $1, $2, $3)")


    @JvmName("crgbaSv3Sd")
    fun ColorRGBa(
        rgb: Symbol<Vector3>,
        a: Symbol<Double> = 1.0.symbol
    ): Symbol<ColorRGBa> =
        functionSymbol(rgb, a, "vec4($0, $1)")

    @JvmName("crgbaSv4")
    fun ColorRGBa(
        rgba: Symbol<Vector4>,
    ): Symbol<ColorRGBa> =
        functionSymbol(rgba,"$0")


    @JvmName("shadeScrgbaSd")
    fun Symbol<ColorRGBa>.shade(factor: Symbol<Double>): Symbol<ColorRGBa> =
        functionSymbol(this, factor, "vec4($0.rgb * $1, $0.a)")

    @JvmName("opacifyScrgbaSd")
    fun Symbol<ColorRGBa>.opacify(factor: Symbol<Double>): Symbol<ColorRGBa> =
        functionSymbol(this, factor, "vec4($0.rgb, $0.a * $1)")

    @JvmName("mixScrgbaSrgbaSb")
    fun Symbol<ColorRGBa>.mix(right: Symbol<ColorRGBa>, factor: Symbol<Boolean>): Symbol<ColorRGBa> =
        functionSymbol(this, right, factor, "mix($0, $1, ($2) ? 1.0 : 0.0)")

    @JvmName("mixScrgbaSrgbaSd")
    fun Symbol<ColorRGBa>.mix(right: Symbol<ColorRGBa>, factor: Symbol<Double>): Symbol<ColorRGBa> =
        functionSymbol(this, right, factor, "mix($0, $1, $2)")

    val Symbol<ColorRGBa>.linear: Symbol<ColorRGBa>
        @JvmName("linearScrgba")
        get() = functionSymbol(this, "pow($0, vec4(1.0/2.2))")

    val Symbol<ColorRGBa>.srgb: Symbol<ColorRGBa>
        @JvmName("srgbScrgba")
        get() = functionSymbol(this, "pow($0, vec4(2.2))")

    val Symbol<ColorRGBa>.r: Symbol<Double>
        @JvmName("rScrgba")
        get() = functionSymbol(this, "$0.r")
    val Symbol<ColorRGBa>.g: Symbol<Double>
        @JvmName("gScrgba")
        get() = functionSymbol(this, "$0.g")

    val Symbol<ColorRGBa>.b: Symbol<Double>
        @JvmName("bScrgba")
        get() = functionSymbol(this, "$0.b")

    val Symbol<ColorRGBa>.a: Symbol<Double>
        @JvmName("aScrgba")
        get() = functionSymbol(this, "$0.a")

    val Symbol<ColorRGBa>.vector4: Symbol<Vector4>
        @JvmName("vector4Scrgba")
        get() = functionSymbol(this, "$0")

    val Symbol<ColorRGBa>.vector3: Symbol<Vector3>
        @JvmName("vector3Scrgba")
        get() = functionSymbol(this, "$0.rgb")


    /**
     * Add support for val a by ColorRGBa.PINK
     */
    operator fun ColorRGBa.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<ColorRGBa> {
        emit("${staticType<ColorRGBa>()} ${property.name} = ${glsl(this)};")
        return ConstantProperty(staticType<ColorRGBa>())
    }
}
