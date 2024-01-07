package org.openrndr.orsl.shadergenerator.dsl.shadestyle

import org.openrndr.draw.ShadeStyle
import org.openrndr.orsl.shadergenerator.annotations.ShaderBookIndex
import org.openrndr.orsl.shadergenerator.dsl.activeGenerator
import org.openrndr.orsl.shadergenerator.phrases.PhraseResolver
import org.openrndr.orsl.shadergenerator.phrases.dsl.ArrayPhrases
import org.openrndr.orsl.shadergenerator.phrases.dsl.ArrayPhrasesIndex
import org.openrndr.orsl.shadergenerator.phrases.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun preprocessor(vararg indices: ShaderBookIndex<*>): PhraseResolver {
    val resolver = PhraseResolver()
    resolver.indices.add(ArrayPhrasesIndex(ArrayPhrases()))
    resolver.indices.add(FastMathPhrasesIndex(FastMathPhrases()))
    resolver.indices.add(ConstPhrasesIndex(ConstPhrases()))
    resolver.indices.add(TransformPhrasesIndex(TransformPhrases()))
    resolver.indices.addAll(indices)
    return resolver
}

/**
 * Create a fragment transform using ORSL
 */
@OptIn(ExperimentalContracts::class)
fun ShadeStyle.fragmentTransform(vararg indices: ShaderBookIndex<*>, f: FragmentTransformBuilder.() -> Unit) {
    contract {
        callsInPlace(f, InvocationKind.EXACTLY_ONCE)
    }

    val builder = FragmentTransformBuilder()
    builder.push()
    require(activeGenerator() == builder)
    builder.f()
    require(activeGenerator() == builder)
    val prep = preprocessor(*indices)

    fragmentPreamble = prep.preprocessShader(builder.preamble)
    fragmentTransform = prep.preprocessShader(builder.code)
    builder.pop()
}

/**
 * Create a vertex transform using ORSL
 */
@OptIn(ExperimentalContracts::class)
fun ShadeStyle.vertexTransform(vararg indices: ShaderBookIndex<*>, f: VertexTransformBuilder.() -> Unit) {
    contract {
        callsInPlace(f, InvocationKind.EXACTLY_ONCE)
    }
    val builder = VertexTransformBuilder()
    builder.push()
    builder.f()
    val prep = preprocessor(*indices)
    vertexPreamble = prep.preprocessShader(builder.preamble)
    vertexTransform = prep.preprocessShader(builder.code)
    builder.pop()
}