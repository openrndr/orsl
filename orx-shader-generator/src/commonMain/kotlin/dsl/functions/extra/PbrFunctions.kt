@file:Suppress("LocalVariableName", "NAME_SHADOWING")

package org.openrndr.extra.shadergenerator.dsl.functions.extra

import kotlin.math.PI
import org.openrndr.extra.shadergenerator.dsl.Generator
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.math.Vector3

interface PbrFunctions : Generator {
    fun pow5(x: Symbol<Double>): Symbol<Double> {
        val pow5 by function<Double, Double>(false) { x ->
            val x2 by x * x
            x2 * x2 * x
        }
        return pow5(x)
    }


    fun D_GGX(linearRoughness: Symbol<Double>, NoH: Symbol<Double>): Symbol<Double> {
        val D_GGX by function<Double, Double, Double>(false) { linearRoughness, NoH ->
            val oneMinusNoHSquared by 1.0 - NoH * NoH
            val a by (NoH * linearRoughness)
            val k by linearRoughness / (oneMinusNoHSquared + a * a)
            val d by k * k * (1.0 / PI)
            d
        }
        return D_GGX(linearRoughness, NoH)
    }

    fun V_SmithGGXCorrelated(
        linearRoughness: Symbol<Double>,
        NoV: Symbol<Double>,
        NoL: Symbol<Double>
    ): Symbol<Double> {
        val V_SmithGGXCorrelated by function<Double, Double, Double, _>(false) { linearRoughness, NoV, NoL ->
            val a2 by linearRoughness * linearRoughness
            val GGXV by NoL * sqrt((NoV - a2 * NoV) * NoV + a2)
            val GGXL by NoV * sqrt((NoL - a2 * NoL) * NoL + a2)
            0.5 / (GGXV + GGXL)
        }
        return V_SmithGGXCorrelated(linearRoughness, NoV, NoL)
    }

    fun F_Schlick(f0: Symbol<Vector3>, VoH: Symbol<Double>): Symbol<Vector3> {
        val F_Schlick by function<Vector3, Double, _>(false){ f0, VoH ->
            f0 + (Vector3.ONE - f0) * pow5(1.0 - VoH)
        }
        return F_Schlick(f0, VoH)
    }

    fun F_Schlick3(f0: Symbol<Double>, f90: Symbol<Double>, VoH: Symbol<Double>): Symbol<Double> {
        val F_Schlick3 by function<Double, Double, Double, _>(false) { f0, f90, VoH ->
            f0 + (f90 - f0) * pow5(1.0 - VoH)
        }
        return F_Schlick3(f0, f90, VoH)
    }

    fun Fd_Burley(linearRoughness: Symbol<Double>, NoV: Symbol<Double>, NoL: Symbol<Double>, LoH: Symbol<Double>): Symbol<Double> {
        val Fd_Burley by function<Double, Double, Double, Double, _>(false) { linearRoughness, NoV, NoL, LoH ->
            val f90 by 0.5 + 2.0 * linearRoughness * LoH * LoH
            val one by 1.0
            val lightScatter by F_Schlick3(one, f90, NoL)
            val viewScatter by F_Schlick3(one, f90, NoV)
            lightScatter * viewScatter * (1.0 / PI)
        }
        return Fd_Burley(linearRoughness, NoV, NoL, LoH)
    }
}
