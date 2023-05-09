package org.openrndr.extra.shadergenerator.phrases.phrases

import org.openrndr.extra.shadergenerator.annotations.IndexShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
class Mod289Phrases : ShaderBook {
    @ShaderPhrase
    val mod289 = "float mod289(const in float x) { return x - floor(x * (1. / 289.)) * 289.; }"

    @ShaderPhrase
    val mod289V2 = "vec2 mod289(const in vec2 x) { return x - floor(x * (1. / 289.)) * 289.; }"

    @ShaderPhrase
    val mod289V3 = "vec3 mod289(const in vec3 x) { return x - floor(x * (1. / 289.)) * 289.; }"

    @ShaderPhrase
    val mod289V4 = "vec4 mod289(const in vec4 x) { return x - floor(x * (1. / 289.)) * 289.; }"
}