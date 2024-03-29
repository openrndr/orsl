package org.openrndr.orsl.shadergenerator.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
class ConstPhrases : ShaderBook{
    @ShaderPhrase
    val PI = "#define PI 3.1415926535897932384626433832795"

    @ShaderPhrase
    val HALF_PI = "#define HALF_PI 1.5707963267948966192313216916398"

    @ShaderPhrase
    val TWO_PI = "#define TWO_PI 6.2831853071795864769252867665590"

    @ShaderPhrase
    val PHI = "#define PHI 1.61803398875"

}