package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Sampler2DArrayFunctions : Generator {

    @JvmName("getSs2aSv3")
    operator fun Symbol<Sampler2DArray>.get(uvw: Symbol<Vector3>): Symbol<Vector4> =
        functionSymbol(this, uvw, "texture($0, $1)")

    @JvmName("getSs2aSv3Sd")
    operator fun Symbol<Sampler2DArray>.get(uvw: Symbol<Vector3>, lod: Symbol<Double>): Symbol<Vector4> =
        Function3Symbol(p0 = this, p1 = uvw, p2 = lod, function = "textureLod($0, $1, $2)", type = "vec4")

    @JvmName("sizeSs2aVi")
    fun Symbol<Sampler2DArray>.size(level: Int = 0): Symbol<IntVector3> =
        functionSymbol(this, level, "textureSize($0, $1)")

    @JvmName("fetchSs2aSv3Vi")
    fun Symbol<Sampler2DArray>.fetch(uvw: Symbol<IntVector3>, lod: Int): Symbol<Vector4> =
        Function3Symbol(p0 = this, p1 = uvw, v2 = lod, function = "texelFetch($0, $1, $2)", type = "vec4")
}