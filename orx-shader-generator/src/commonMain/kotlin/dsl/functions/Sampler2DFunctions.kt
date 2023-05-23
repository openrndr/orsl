package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.Vector4

interface Sampler2DFunctions : Generator {
    operator fun Symbol<Sampler2D>.get(uv: Symbol<Vector2>): Symbol<Vector4> =
        functionSymbol(this, uv, "texture($0, $1)")

    operator fun Symbol<Sampler2D>.get(uv: Symbol<Vector2>, lod: Symbol<Double>): Symbol<Vector4> =
        FunctionSymbol3(p0 = this, p1 = uv, p2 = lod, function = "textureLod($0, $1, $2)", type = "vec4")

    fun Symbol<Sampler2D>.size(level: Int = 0): Symbol<IntVector2> =
        functionSymbol(this, level, "textureSize($0, $1)")

    fun Symbol<Sampler2D>.fetch(uv: Symbol<IntVector2>, lod: Int): Symbol<Vector4> =
        FunctionSymbol3(p0 = this, p1 = uv, v2 = lod, function = "texelFetch($0, $1, $2)", type = "vec4")
}