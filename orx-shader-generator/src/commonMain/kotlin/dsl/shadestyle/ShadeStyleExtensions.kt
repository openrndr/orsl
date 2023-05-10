package org.openrndr.extra.shadergenerator.phrases.dsl.shadestyle

import org.openrndr.draw.ShadeStyle
import org.openrndr.extra.shadergenerator.phrases.PhraseResolver
import org.openrndr.extra.shadergenerator.phrases.dsl.ArrayPhrases
import org.openrndr.extra.shadergenerator.phrases.dsl.ArrayPhrasesIndex
import org.openrndr.extra.shadergenerator.phrases.phrases.*

private fun preprocessor():PhraseResolver {
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
    val prep = preprocessor()
    val builder = FragmentTransformBuilder()
    builder.f()
    fragmentPreamble = prep.preprocessShader(builder.preamble)
    fragmentTransform = prep.preprocessShader(builder.code)
}