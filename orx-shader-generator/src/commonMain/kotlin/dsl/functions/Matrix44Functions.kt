package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.Matrix33
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Matrix44Functions : Generator {
    operator fun Symbol<Matrix44>.times(right: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, right, "($0 * $1)")

    val Symbol<Matrix44>.inversed: Symbol<Matrix44>
        @JvmName("inverseSm4")
        get() = functionSymbol(this, "inverse($0)")

    val Symbol<Matrix44>.determinant: Symbol<Double>
        @JvmName("determinantSm4")
        get() = functionSymbol(this, "determinant($0)")

    @JvmName("getSm4Vi")
    operator fun Symbol<Matrix44>.get(column: Int): Symbol<Vector4> =
        functionSymbol(this, column, function = "$0[$1]")

    @JvmName("setSm4ViSv4")
    operator fun Symbol<Matrix44>.set(column: Int, value: Symbol<Vector4>): Symbol<Vector4> =
        functionSymbol(this, column, value, function = "$0[$1] = $2")

    fun Matrix44.Companion.fromColumnVectors(x: Symbol<Vector4>, y: Symbol<Vector4>, z: Symbol<Vector4>, w: Symbol<Vector4>): Symbol<Matrix44> =
        functionSymbol(x, y, z, "mat4($0, $1, $2, $3)")


}