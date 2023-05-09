package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.Vector4

interface Sampler2DFunctions : Generator {
    operator fun Symbol<Sampler2D>.get(uv: Symbol<Vector2>): Symbol<Vector4> {
        return FunctionSymbol2(p0 = this, p1 = uv, function = "texture($0, $1)")
    }

    operator fun Symbol<Sampler2D>.get(uv: Symbol<Vector2>, lod: Symbol<Double>): Symbol<Vector4> {
        return FunctionSymbol3(p0 = this, p1 = uv, p2 = lod, function = "textureLod($0, $1, $2)")
    }

    fun Symbol<Sampler2D>.size(level: Int = 0): Symbol<IntVector2> {
        return FunctionSymbol2(p0 = this, v1 = level, function = "textureSize($0, $1)")
    }

    fun Symbol<Sampler2D>.fetch(uv: Symbol<IntVector2>, lod: Int): Symbol<Vector4> {
        return FunctionSymbol3(p0 = this, p1 = uv, v2 = lod, function = "texelFetch($0, $1, $2)")
    }
}