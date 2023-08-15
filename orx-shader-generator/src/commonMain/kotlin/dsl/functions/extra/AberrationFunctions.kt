package org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.math.Vector3

fun ShaderBuilder.aberrationColor(f : Symbol<Double>) : Symbol<Vector3> {
    val aberrationColor by function<Double, Vector3> { g ->
        val f by g * 3.0 - 1.5
        Vector3(-f, 1.0 - abs(f), f).min(Vector3(1.0)).max(Vector3(0.0))
    }
    return aberrationColor(f)
}