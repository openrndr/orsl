package org.openrndr.extra.shadergenerator.phrases
import org.openrndr.extra.shadergenerator.annotations.IndexShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderPhrase
import org.openrndr.extra.shadergenerator.annotations.WrapShaderBook

@IndexShaderBook
@WrapShaderBook
class MyShaderPhrases: ShaderBook {

    @ShaderPhrase
    val hash22 = """vec2 hash22(vec2 x) {
        return vec2(0.1, 0.2);
        }
    """

    val hash23 = """vec3 hash23(vec2 x) {
        return vec2(0.4, 0.5);
        }
    """
}