package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntVectorFunctions {

    @JvmName("toDoubleSiv2")
    fun Symbol<IntVector2>.toDouble(): Symbol<Vector2> = FunctionSymbol1(p0 = this, function = "vec2($0)")
    @JvmName("toDoubleSiv3")
    fun Symbol<IntVector3>.toDouble(): Symbol<Vector3> = FunctionSymbol1(p0 = this, function = "vec3($0)")
    @JvmName("toDoubleSiv4")
    fun Symbol<IntVector4>.toDouble(): Symbol<Vector4> = FunctionSymbol1(p0 = this, function = "vec4($0)")


    val Symbol<IntVector2>.x: Symbol<Int>
        @JvmName("xSiv2")
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<IntVector2>.y: Symbol<Int>
        @JvmName("ySiv2")
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<IntVector3>.x: Symbol<Int>
        @JvmName("xSiv3")
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<IntVector3>.y: Symbol<Int>
        @JvmName("ySiv3")
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<IntVector3>.z: Symbol<Int>
        @JvmName("zSiv3")
        get() = FunctionSymbol1(p0 = this, function = "$0.z")


    val Symbol<IntVector4>.x: Symbol<Int>
        @JvmName("xSiv4")
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<IntVector4>.y: Symbol<Int>
        @JvmName("ySiv4")
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<IntVector4>.z: Symbol<Int>
        @JvmName("zSiv4")
        get() = FunctionSymbol1(p0 = this, function = "$0.z")

    val Symbol<IntVector4>.w: Symbol<Int>
        @JvmName("wSiv4")
        get() = FunctionSymbol1(p0 = this, function = "$0.w")

    val Symbol<IntVector4>.xyz: Symbol<IntVector3>
        @JvmName("xyzSiv4")
        get() = FunctionSymbol1(p0 = this, function = "$0.xyz")
}