package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol2
import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol3
import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol
import org.openrndr.math.*
import kotlin.jvm.JvmName

interface Functions {
    fun <T : EuclideanVector<T>> cos(x: Symbol<T>) = FunctionSymbol1<T, T>(p0 = x, function = "cos($0)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("cosD")

    fun cos(x: Symbol<Double>) = FunctionSymbol1<Double, Double>(p0 = x, function = "cos($0)")


    fun ((Symbol<Double>) -> FunctionSymbol1<Double, Double>).ref(): FunctionSymbol1<Double, Double> {
        return this(object : Symbol<Double> {
            override val name: String
                get() = "$0"
        })
    }

    fun <T : EuclideanVector<T>> sin(x: Symbol<T>) = FunctionSymbol1<T, T>(p0 = x, function = "sin($0)")
    fun <T : EuclideanVector<T>> abs(x: Symbol<T>): Symbol<T> = FunctionSymbol1(p0 = x, function = "abs($0)")
    fun <T : EuclideanVector<T>> sqrt(x: Symbol<T>): Symbol<T> = FunctionSymbol1(p0 = x, function = "sqrt($0)")


    fun min(a: Symbol<Double>, b: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = a, p1 = b, function = "min($0, $1)")

    fun max(a: Symbol<Double>, b: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = a, p1 = b, function = "max($0, $1)")


    val Symbol<Vector2>.x: Symbol<Double>
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<Vector3>.x: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("xSV3")
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<Vector4>.x: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("xSV4")
        get() = FunctionSymbol1(p0 = this, function = "$0.x")

    val Symbol<Vector2>.y: Symbol<Double>
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<Vector3>.y: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("ySV3")
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<Vector4>.y: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("ySV4")
        get() = FunctionSymbol1(p0 = this, function = "$0.y")

    val Symbol<Vector3>.z: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("zSV3")
        get() = FunctionSymbol1(p0 = this, function = "$0.z")

    val Symbol<Vector4>.z: Symbol<Double>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("zSV4")
        get() = FunctionSymbol1(p0 = this, function = "$0.z")

    val Symbol<Vector4>.w: Symbol<Double>
        get() = FunctionSymbol1(p0 = this, function = "$0.w")


    val Symbol<Vector3>.xy: Symbol<Vector2>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("xySV3")
        get() = FunctionSymbol1(p0 = this, function = "$0.xy")


    val Symbol<Vector4>.xy: Symbol<Vector3>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("xySV4")
        get() = FunctionSymbol1(p0 = this, function = "$0.xy")

    val Symbol<Vector4>.xyz: Symbol<Vector3>
        @Suppress("INAPPLICABLE_JVM_NAME") @JvmName("xyzSV4")
        get() = FunctionSymbol1(p0 = this, function = "$0.xyz")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("plusSDSD")
    operator fun Symbol<Double>.plus(right: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 + $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("minusSDSD")
    operator fun Symbol<Double>.minus(right: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 - $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSDSD")
    operator fun Symbol<Double>.times(right: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSDVD")
    operator fun Symbol<Double>.times(right: Double): Symbol<Double> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 * $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("plusSdVd")
    operator fun Symbol<Double>.plus(right: Double): Symbol<Double> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 + $1)")


    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("plusSiVi")
    operator fun Symbol<Int>.plus(right: Int): Symbol<Int> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 + $1)")

    operator fun Symbol<Int>.unaryMinus(): Symbol<Int> =
        FunctionSymbol1(p0 = this, function = "(-$0)")


    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("divSDSD")
    operator fun Symbol<Double>.div(right: Symbol<Double>): Symbol<Double> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 / $1)")

    operator fun <T : EuclideanVector<T>> Symbol<T>.plus(right: Symbol<T>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 + $1)")

    operator fun <T : EuclideanVector<T>> Symbol<T>.minus(right: Symbol<T>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 - $1)")

    operator fun <T : EuclideanVector<T>> Symbol<T>.minus(right: T): Symbol<T> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 - $1)")


    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSEVSEV")
    operator fun <T : EuclideanVector<T>> Symbol<T>.times(right: Symbol<T>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")


    operator fun <T : EuclideanVector<T>> Symbol<T>.times(right: Double): Symbol<T> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 * $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSEVI")
    operator fun <T : EuclideanVector<T>> Symbol<T>.times(right: Symbol<Int>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSiv2Vd")
    operator fun Symbol<IntVector2>.times(right: Double): Symbol<Vector2> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 * $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSiv2Sd")
    operator fun Symbol<IntVector2>.times(right: Symbol<Double>): Symbol<Vector2> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")

    operator fun Double.div(right: Symbol<IntVector2>): Symbol<Vector2> =
        FunctionSymbol2(v0 = this, p1 = right, function = "($0 / $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("timesSD")
    operator fun <T : EuclideanVector<T>> Symbol<T>.times(right: Symbol<Double>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")


    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("divSEV")
    operator fun <T : EuclideanVector<T>> Symbol<T>.div(right: Symbol<T>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 / $1)")

    operator fun <T : EuclideanVector<T>> Symbol<T>.div(right: Double): Symbol<T> =
        FunctionSymbol2(p0 = this, v1 = right, function = "($0 / $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("divSD")
    operator fun <T : EuclideanVector<T>> Symbol<T>.div(right: Symbol<Double>): Symbol<T> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 / $1)")

    val <T : EuclideanVector<T>> Symbol<T>.length: Symbol<Double>
        get() = FunctionSymbol1(p0 = this, function = "length($0)")

    fun <T : EuclideanVector<T>> Symbol<T>.dot(right: Symbol<T>): Symbol<Double> =
        FunctionSymbol2(p0 = this, p1 = right, function = "dot($0, $1)")

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("smoothstepSD")
    fun smoothstep(edge0: Symbol<Double>, edge1: Symbol<Double>, x: Symbol<Double>): Symbol<Double> =
        FunctionSymbol3(p0 = edge0, p1 = edge1, p2 = x, function = "smoothstep($0, $1, $2)")

    fun smoothstep(edge0: Double, edge1: Double, x: Symbol<Double>): Symbol<Double> =
        FunctionSymbol3(v0 = edge0, v1 = edge1, p2 = x, function = "smoothstep($0, $1, $2)")


    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("smoothstepSEV")
    fun <T : EuclideanVector<T>> smoothstep(edge0: Symbol<T>, edge1: Symbol<T>, x: Symbol<T>): Symbol<T> =
        FunctionSymbol3(p0 = edge0, p1 = edge1, p2 = x, function = "smoothstep($0, $1, $2)")

    fun Vector3(xy: Symbol<Vector2>, z: Symbol<Double>) = object : Symbol<Vector3> {
        override val name: String = "vec3(${xy.name}, ${z.name})"
    }


    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Symbol<Double>) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, ${w.name})"
    }

    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Double) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, $w)"
    }

}