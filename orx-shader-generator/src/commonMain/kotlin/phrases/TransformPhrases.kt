package org.openrndr.extra.shadergenerator.phrases

import org.openrndr.extra.shadergenerator.annotations.IndexShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderPhrase
import org.openrndr.extra.shadergenerator.annotations.WrapShaderBook

@IndexShaderBook
@WrapShaderBook
class TransformPhrases : ShaderBook{
    @ShaderPhrase
    val erot = """vec3 erot(vec3 p, vec3 ax, float ro) {
    return mix(dot(p,ax)*ax,p,cos(ro))+sin(ro)*cross(ax,p);
}"""
}