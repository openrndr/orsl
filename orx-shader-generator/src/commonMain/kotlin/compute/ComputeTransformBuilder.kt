package org.openrndr.extra.shadergenerator.compute

import org.openrndr.extra.shadergenerator.dsl.functions.extra.PbrFunctions
import org.openrndr.extra.shadergenerator.dsl.shadestyle.ShadeStyleBuilder
import org.openrndr.extra.shadergenerator.phrases.*
import org.openrndr.extra.shadergenerator.dsl.functions.FragmentDerivativeFunctions
import org.openrndr.extra.shadergenerator.dsl.shadestyle.preprocessor
import org.openrndr.extra.shadergenerator.dsl.functions.Image2DFunctions
import org.openrndr.extra.shadergenerator.dsl.functions.IntRImage2DFunctions
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

class ComputeTransformBuilder() : ShadeStyleBuilder(), HashPhrasesFunctions, ValueNoiseDerPhrasesFunctions,
    SimplexPhrasesFunctions, SdfPhrasesFunctions, FibonacciPhrasesFunctions, PbrFunctions, FragmentDerivativeFunctions,

Image2DFunctions, IntRImage2DFunctions {
}

fun ComputeStyle.computeTransform(builder: ComputeTransformBuilder.() ->Unit) {
    val ctb = ComputeTransformBuilder()
    ctb.push()
    ctb.builder()
    val prep = preprocessor()
    computePreamble = prep.preprocessShader(ctb.preamble)
    computeTransform = prep.preprocessShader(ctb.code)
    ctb.pop()
}