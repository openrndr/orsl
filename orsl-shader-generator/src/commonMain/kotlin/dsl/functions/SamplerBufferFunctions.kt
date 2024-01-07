package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.Generator

import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface SamplerBufferFunctions : Generator {
    @JvmName("sizeSsb")
    fun Symbol<SamplerBuffer>.size(): Symbol<Int> =
        functionSymbol(this, "textureSize($0)")

    @JvmName("fetchSsbSi")
    fun Symbol<SamplerBuffer>.fetch(u: Symbol<Int>): Symbol<Vector4> =
        Function2Symbol(p0 = this, p1 = u, function = "texelFetch($0, $1)", type = "vec4")
}