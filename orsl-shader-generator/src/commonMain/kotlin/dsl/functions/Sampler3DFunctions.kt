package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.Generator
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Sampler3DFunctions : Generator {
    @JvmName("getSs3Sv3")
    operator fun Symbol<Sampler3D>.get(uvw: Symbol<Vector3>): Symbol<Vector4> =
        functionSymbol(this, uvw, "texture($0, $1)")

    @JvmName("getSs3Sv3Sd")
    operator fun Symbol<Sampler3D>.get(uvw: Symbol<Vector3>, lod: Symbol<Double>): Symbol<Vector4> =
        Function3Symbol(p0 = this, p1 = uvw, p2 = lod, function = "textureLod($0, $1, $2)", type = "vec4")

    @JvmName("sizeSs3Vi")
    fun Symbol<Sampler3D>.size(level: Int = 0): Symbol<IntVector3> =
        functionSymbol(this, level, "textureSize($0, $1)")

    @JvmName("fetchSs3Sv2Vi")
    fun Symbol<Sampler3D>.fetch(uv: Symbol<IntVector2>, lod: Symbol<Int>): Symbol<Vector4> =
        functionSymbol(this, uv, lod, "texelFetch($0, $1)")

}