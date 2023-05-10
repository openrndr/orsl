package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector4

interface Matrix44Functions : Generator {
    operator fun Symbol<Matrix44>.times(right : Symbol<Vector4>) : Symbol<Vector4> =
        FunctionSymbol2(p0 = this, p1 = right, function = "($0 * $1)")
    val Symbol<Matrix44>.inversed: Symbol<Matrix44>
        get() = FunctionSymbol1(p0 = this, function = "inverse($0)")

    operator fun Symbol<Matrix44>.get(column: Int): Symbol<Vector4> =
        FunctionSymbol2(p0 = this, v1 = column, function = "$0[$1]")

    operator fun Symbol<Matrix44>.set(column: Int, value: Symbol<Vector4>): Symbol<Vector4> =
        FunctionSymbol3(p0 = this, v1 = column, p2 = value, function = "$0[$1] = $2")
}