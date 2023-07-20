package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.IntVector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface RImage3DFunctions : Generator{
    @JvmName("loadSri3Siv3")
    fun Symbol<RImage3D>.load(uvw: Symbol<IntVector3>): Symbol<Double> =
        functionSymbol(this, uvw, "imageLoad($0, $1).x")

    @JvmName("storeSri3Siv3i")
    fun Symbol<RImage3D>.store(uvw: Symbol<IntVector3>, data: Symbol<Double>) {
        functionCall(this, uvw, data, "imageStore($0, $1, vec4($2))")
    }

    @JvmName("sizeSri3")
    fun Symbol<RImage3D>.size(): Symbol<IntVector3> =
        functionSymbol(this, "imageSize($0)")


}