package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.FunctionSymbol1
import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.Functions.FunctionPropertyProvider
import org.openrndr.extra.shadergenerator.dsl.staticType
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName


@JvmName("perturbSd")
inline fun <reified R> ShaderBuilder.perturb(
    noinline function: (x: Symbol<Double>) -> FunctionSymbol1<Double, R>,
    noinline distort: (x: Symbol<Double>) -> FunctionSymbol1<Double, Double>,
): FunctionPropertyProvider<Double, R> {
    return FunctionPropertyProvider(true, this@perturb, staticType<Double>(), staticType<R>()) {
        val coord by distort(it)
        function(coord)
    }
}

@JvmName("perturbSv2")
inline fun <reified R> ShaderBuilder.perturb(
    noinline function: (x: Symbol<Vector2>) -> FunctionSymbol1<Vector2, R>,
    noinline distort: (x: Symbol<Vector2>) -> FunctionSymbol1<Vector2, Vector2>,
): FunctionPropertyProvider<Vector2, R> {
    return FunctionPropertyProvider(true, this@perturb, staticType<Vector2>(), staticType<R>()) {
        val coord by distort(it)
        function(coord)
    }
}

@JvmName("perturbSv3")
inline fun <reified R> ShaderBuilder.perturb(
    noinline function: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, R>,
    noinline distort: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Vector3>,
): FunctionPropertyProvider<Vector3, R> {
    return FunctionPropertyProvider(true, this@perturb, staticType<Vector3>(), staticType<R>()) {
        val coord by distort(it)
        function(coord)
    }
}

@JvmName("perturbSv4")
inline fun <reified R> ShaderBuilder.perturb(
    noinline function: (x: Symbol<Vector4>) -> FunctionSymbol1<Vector4, R>,
    noinline distort: (x: Symbol<Vector4>) -> FunctionSymbol1<Vector4, Vector4>,
): FunctionPropertyProvider<Vector4, R> {
    return FunctionPropertyProvider(true, this@perturb, staticType<Vector4>(), staticType<R>()) {
        val coord by distort(it)
        function(coord)
    }
}