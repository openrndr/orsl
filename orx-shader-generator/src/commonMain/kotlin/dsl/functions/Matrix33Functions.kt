package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.math.Matrix33
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Matrix33Functions : Generator  {

    @JvmName("timesSm3Sv3")
    operator fun Symbol<Matrix33>.times(right: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSm3Sm3")
    operator fun Symbol<Matrix33>.times(right: Symbol<Matrix33>): Symbol<Matrix33> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSm3Sd")
    operator fun Symbol<Matrix33>.times(right: Symbol<Double>): Symbol<Matrix33> =
        functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSm3Vd")
    operator fun Symbol<Matrix33>.times(right: Double): Symbol<Matrix33> =
        functionSymbol(this, right, "($0 * $1)")


    val Symbol<Matrix33>.inversed: Symbol<Matrix33>
        @JvmName("inversedSm3")
        get() = functionSymbol(this, "inverse($0)")

    val Symbol<Matrix33>.determinant: Symbol<Double>
        @JvmName("determinantSm3")
        get() = functionSymbol(this, "determinant($0)")


    @JvmName("getSm3Vi")
    operator fun Symbol<Matrix33>.get(column: Int): Symbol<Vector3> =
        functionSymbol(this, column, function = "$0[$1]")

    @JvmName("setSm3ViSv3")
    operator fun Symbol<Matrix33>.set(column: Int, value: Symbol<Vector3>): Symbol<Vector3> =
        functionSymbol(this, column, value, function = "$0[$1] = $2")

    fun Matrix33.Companion.fromColumnVectors(x: Symbol<Vector3>, y: Symbol<Vector3>, z: Symbol<Vector3>): Symbol<Matrix33> =
        functionSymbol(x, y, z, "mat3($0, $1, $2)")

}