package org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function

/**
 * Calculate the circle of confusion
 * @param aperture unitless aperture of the lens
 * @param focalLength unitless focal length of the lens
 * @param focus unitless distance at which the lens is focussed
 * @param distance unitless distance at which the object is placed
 */
fun ShaderBuilder.coc(
    aperture: Symbol<Double>,
    focalLength: Symbol<Double>,
    focus: Symbol<Double>,
    distance: Symbol<Double>
): Symbol<Double> {
    val coc by function<Double, Double, Double, Double, Double> { a, f, zf, z ->
        abs((a * f * (zf - z)) / (zf * (z - f)))
    }
    return coc(aperture, focalLength, focus, distance)
}