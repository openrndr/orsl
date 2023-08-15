package org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.math.Matrix33
import org.openrndr.math.Vector3

fun ShaderBuilder.normalRotate(point: Symbol<Vector3>, direction: Symbol<Vector3>): Symbol<Vector3> {
    val clipRotate by function<Vector3, Vector3, Vector3> { point, direction ->
        val dOx by abs(direction.dot(Vector3.UNIT_X.symbol)) lt 0.99
        val up by if_(dOx) { direction.cross(Vector3.UNIT_X.symbol).normalized } else_ {
            direction.cross(Vector3.UNIT_Y.symbol).normalized
        }
        val right by up.cross(direction)
        val r by Matrix33.fromColumnVectors(right, up, direction) * point
        r
    }
    return clipRotate(point, direction)
}