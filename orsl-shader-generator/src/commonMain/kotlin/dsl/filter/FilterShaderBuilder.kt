package org.openrndr.orsl.shadergenerator.dsl.filter

import org.openrndr.draw.Shader
import org.openrndr.draw.filterShaderFromCode
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.ShadeStyleBuilder
import org.openrndr.math.Vector2
import org.openrndr.math.Vector4

class FilterShaderBuilder : ShaderBuilder(emptySet()) {
    inline fun <reified T> uniform(): ShadeStyleBuilder.ParameterProperty<T> {
        return ShadeStyleBuilder.ParameterProperty(staticType<T>())
    }



    val tex0 by uniform<Sampler2D>()
    val tex1 by uniform<Sampler2D>()
    val tex2 by uniform<Sampler2D>()
    val tex3 by uniform<Sampler2D>()

    val v_texCoord0 by uniform<Vector2>()
    var o_output by output<Vector4>()

    fun generateShader() : Shader {
        val shader = """

uniform sampler2D tex0;
uniform sampler2D tex1;
uniform sampler2D tex2;
uniform sampler2D tex3;            
            
in vec2 v_texCoord0;
out vec4 o_output;
            
$preamble
            
void main() {
${code.prependIndent("   ")}
}
        """
        return filterShaderFromCode(shader, "orsl-generated-shader")
    }
}

fun buildFilterShader(builder: FilterShaderBuilder.() -> Unit) : Shader {
    val fsb = FilterShaderBuilder()

    fsb.builder()
    return fsb.generateShader()

}