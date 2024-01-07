package org.openrndr.orsl.shadergenerator.phrases.sdf

import org.openrndr.draw.Struct
import org.openrndr.orsl.shadergenerator.dsl.FunctionSymbol1
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.Functions
import org.openrndr.orsl.shadergenerator.dsl.staticType
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.orsl.shadergenerator.dsl.structs.setValue
import org.openrndr.math.Vector3

class MarchResult : Struct<MarchResult>() {
    var position by field<Vector3>()
    var hit by field<Boolean>()
    var travel by field<Double>()
    var normal by field<Vector3>()
}

var Symbol<MarchResult>.position by MarchResult::position
var Symbol<MarchResult>.hit by MarchResult::hit
var Symbol<MarchResult>.travel by MarchResult::travel
var Symbol<MarchResult>.normal by MarchResult::normal

fun ShaderBuilder.march(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    iterations: Int = 300,
    tolerance: Double = 1E-2,
    stepScale: Double = 0.5
) = Functions.Function2PropertyProvider<Vector3, Vector3, _>(
    true,
    this@march, staticType<Vector3>(),
    staticType<Vector3>(),
    staticType<MarchResult>()
) { origin, direction ->
    val result by MarchResult()
    val False by false
    val True by true
    result.hit = False
    var position by variable(origin)

    val i by variable<Int>()
    i.for_(0 until iterations) {
        val distance by scene(position)
        doIf(abs(distance) lt tolerance) {
            result.hit = True
            result.position = position
            break_()
        }
        position += direction * distance * stepScale
    }
    result
}




fun ShaderBuilder.calcAO(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    iterations: Int = 5,
    bias: Double = 0.01,
    distance: Double = 0.15,
    falloff : Double = 0.95,
    intensity: Double = 1.5
): Functions.Function2PropertyProvider<Vector3, Vector3, Double> {
    return Functions.Function2PropertyProvider(true,
        this@calcAO, staticType<Vector3>(),
        staticType<Vector3>(),
        staticType<Double>()
    ) { origin, direction ->
        var occ by variable(0.0)
        var sca by variable(1.0)

        val i by variable(0)
        i.for_(0 until iterations) {
            val h by bias + distance * (i / (iterations - 1.0))
            val d by scene(origin + h * direction)
            occ += (h - d) * sca
            sca *= falloff
        }
        saturate(1.0 - intensity * occ)
    }
}

fun ShaderBuilder.calcSoftShadow(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    iterations: Int = 32,
): Functions.Function4PropertyProvider<Vector3, Vector3, Double, Double, Double> {
    return Functions.Function4PropertyProvider(
        true,
        this@calcSoftShadow, staticType<Vector3>(),
        staticType<Vector3>(),
        staticType<Double>(),
        staticType<Double>(),
        staticType<Double>()
    ) { origin, direction, mint, tmax ->
        var res by variable(1.0)
        var t by variable(mint)
        var ph by variable(1E10)

        val i by variable(0)
        i.for_(0 until iterations) {
            val h by scene(origin + direction * t)
            val y by h * h / (2.0 * ph)
            val d by sqrt(h * h - y * y);
            res = min(res, 10.0 * d / max(0.0, t - y));
            ph = h
            doIf(res lt 0.0001 or (t gt tmax)) {
                break_()
            }
            t += h

        }

        res = saturate(res)
        res * res * (3.0 - 2.0 * res)


    }
}

fun ShaderBuilder.calcShadow(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>,
    iterations: Int = 32,
): Functions.Function4PropertyProvider<Vector3, Vector3, Double, Double, Double> {
    return Functions.Function4PropertyProvider(
        true, this@calcShadow, staticType<Vector3>(),
        staticType<Vector3>(),
        staticType<Double>(),
        staticType<Double>(),
        staticType<Double>()
    ) { origin, direction, mint, tmax ->
        var res by variable(1.0)
        var t by variable(mint)

        val i by variable(0)
        i.for_(0 until iterations) {
            val h by scene(origin + direction * t)
            doIf(h lt 1E-4) {
                val z by 0.0
                res = z
                break_()
            }
            doIf((t gt tmax)) {
                break_()
            }
            t += h * 0.5
        }
        res

    }
}