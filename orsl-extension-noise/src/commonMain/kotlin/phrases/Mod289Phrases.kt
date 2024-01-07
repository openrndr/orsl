package org.openrndr.orsl.extension.noise.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase
import org.openrndr.orsl.shadergenerator.annotations.WrapShaderBook

@IndexShaderBook
//@WrapShaderBook(extensionFunctions = true)
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