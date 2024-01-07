package org.openrndr.orsl.shadergenerator.phrases
import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase
import org.openrndr.orsl.shadergenerator.annotations.WrapShaderBook

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