package org.openrndr.orsl.shadergenerator.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
class PermutePhrases : ShaderBook {

    @ShaderPhrase
    val permute = "float permute(const in float x) { return mod289(((x * 34.0) + 1.0) * x); }"

    @ShaderPhrase
    val permuteV2 = "vec2 permute(const in vec2 x) { return mod289(((x * 34.0) + 1.0) * x); }"

    @ShaderPhrase
    val permuteV3 = "vec3 permute(const in vec3 x) { return mod289(((x * 34.0) + 1.0) * x); }"

    @ShaderPhrase
    val permuteV4 = "vec4 permute(const in vec4 x) { return mod289(((x * 34.0) + 1.0) * x); }"
}