package org.openrndr.orsl.shadergenerator.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
class FastMathPhrases : ShaderBook {
    @ShaderPhrase
    val taylorInvSqrt = "float taylorInvSqrt(in float r) { return 1.79284291400159 - 0.85373472095314 * r; }"

    @ShaderPhrase
    val taylorInvSqrtV2 = "vec2 taylorInvSqrt(in vec2 r) { return 1.79284291400159 - 0.85373472095314 * r; }"

    @ShaderPhrase
    val taylorInvSqrtV3 = "vec3 taylorInvSqrt(in vec3 r) { return 1.79284291400159 - 0.85373472095314 * r; }"

    @ShaderPhrase
    val taylorInvSqrtV4 = "vec4 taylorInvSqrt(in vec4 r) { return 1.79284291400159 - 0.85373472095314 * r; }"


}