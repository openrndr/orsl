package org.openrndr.extra.shadergenerator.dsl.shadestyle

import org.openrndr.draw.ShadeStyle
import org.openrndr.extra.shadergenerator.phrases.PhraseResolver
import org.openrndr.extra.shadergenerator.phrases.dsl.ArrayPhrases
import org.openrndr.extra.shadergenerator.phrases.dsl.ArrayPhrasesIndex
import org.openrndr.extra.shadergenerator.phrases.*

private fun preprocessor(): PhraseResolver {
    val resolver = PhraseResolver()
    resolver.indices.add(ArrayPhrasesIndex(ArrayPhrases()))
    resolver.indices.add(HashPhrasesIndex(HashPhrases()))
    resolver.indices.add(FastMathPhrasesIndex(FastMathPhrases()))
    resolver.indices.add(Mod289PhrasesIndex(Mod289Phrases()))
    resolver.indices.add(PermutePhrasesIndex(PermutePhrases()))
    resolver.indices.add(SimplexPhrasesIndex(SimplexPhrases()))
    resolver.indices.add(ConstPhrasesIndex(ConstPhrases()))
    resolver.indices.add(EasingPhrasesIndex(EasingPhrases()))
    return resolver
}

fun ShadeStyle.fragmentTransform(f: FragmentTransformBuilder.() -> Unit) {
    val builder = FragmentTransformBuilder()
    builder.push()
    builder.f()
    val prep = preprocessor()
    fragmentPreamble = prep.preprocessShader(builder.preamble)
    fragmentTransform = prep.preprocessShader(builder.code)
    builder.pop()
}

fun ShadeStyle.vertexTransform(f: VertexTransformBuilder.() -> Unit) {
    val builder = VertexTransformBuilder()
    builder.push()
    builder.f()
    val prep = preprocessor()
    vertexPreamble = prep.preprocessShader(builder.preamble)
    vertexTransform = prep.preprocessShader(builder.code)
    builder.pop()
}