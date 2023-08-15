package org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.math.Vector3

// from https://www.shadertoy.com/view/Ms33zj
// http://home.hiroshima-u.ac.jp/kin/publications/TVC01/examples.pdf

fun ShaderBuilder.physhue2rgb(hue: Symbol<Double>, ratio: Symbol<Double>): Symbol<Vector3> {
    val physhue2rgb by function<Double, Double, Vector3> { hue, ratio ->
        smoothstep(
            Vector3(0.0).symbol,
            Vector3(1.0).symbol,
            abs(mod(Vector3(hue, hue, hue) + Vector3(0.0, 1.0, 2.0) * ratio, Vector3(1.0).symbol) * 2.0 - Vector3(1.0))
        )
    }
    return physhue2rgb(hue, ratio)
}

fun ShaderBuilder.iridescence(angle: Symbol<Double>, thickness: Symbol<Double>) : Symbol<Vector3> {

    val iridescence by function<Double, Double, Vector3> { angle, thickness ->
        val NxV by cos(angle)
        // energy of spectral colors
        val lum by 0.05064
        // tint of the final color
        val luma by 0.01070

        val tint by Vector3(0.49639, 0.78252, 0.88723)
        // interference rate at minimum angle
        val interf0 by 2.4
        // phase shift rate at minimum angle
        val phase0 by 1.0 / 2.8;
        // interference rate at maximum angle
        val interf1 by interf0 * 4.0 / 3.0;
        // phase shift rate at maximum angle

        val phase1 by phase0
        // fresnel (most likely completely wrong)
        val f by (1.0 - NxV) * (1.0 - NxV)
        val interf by interf0.mix(interf1, f)
        val phase by phase0.mix(phase1, f)
        val dp by (NxV - 1.0) * 0.5

        val c0 by physhue2rgb(thickness * interf0 + dp, thickness * phase0)
        val c1 by physhue2rgb(thickness * interf1 + 0.1 + dp, thickness * phase1)

        val hue by c0.mix(c1, f)
        val film by hue * lum + Vector3(0.49639, 0.78252, 0.88723) * luma
        film * 3.0 + pow(f, 12.0.symbol) * tint
    }
    return iridescence(angle, thickness)
}
