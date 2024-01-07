package org.openrndr.orsl.shadergenerator.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.Generator
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface SamplerCubeFunctions : Generator {

    @JvmName("getSscSv3")
    operator fun Symbol<SamplerCube>.get(uvw: Symbol<Vector3>): Symbol<Vector4> =
        functionSymbol(this, uvw, "texture($0, $1)")

    @JvmName("getSscSv3Sd")
    operator fun Symbol<SamplerCube>.get(uvw: Symbol<Vector3>, lod: Symbol<Double>): Symbol<Vector4> =
        Function3Symbol(p0 = this, p1 = uvw, p2 = lod, function = "textureLod($0, $1, $2)", type = "vec4")

    @JvmName("sizeSsc")
    fun Symbol<SamplerCube>.size(level: Int = 0): Symbol<IntVector2> =
        functionSymbol(this, level, "textureSize($0, $1)")

}