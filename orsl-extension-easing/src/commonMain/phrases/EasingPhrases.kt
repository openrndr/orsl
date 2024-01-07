package org.openrndr.orsl.shadergenerator.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
@WrapShaderBook(extensionFunctions = true)
class EasingPhrases : ShaderBook {
    @ShaderPhrase
    val backIn = """float backIn(in float t) {
    return pow(t, 3.) - t * sin(t * 3.1415926535897932384626433832795);
}"""

    @ShaderPhrase
    val backOut = """#pragma import backIn
float backOut(in float t) {
    return 1. - backIn(1. - t);
}"""

    @ShaderPhrase
    val backInOut = """float backInOut(in float t) {
    float f = t < .5
        ? 2.0 * t
        : 1.0 - (2.0 * t - 1.0);

    float g = backIn(f);

    return t < 0.5
        ? 0.5 * g
        : 0.5 * (1.0 - g) + 0.5;
}"""

    @ShaderPhrase
    val bounceOut = """float bounceOut(in float t) {
    const float a = 4.0 / 11.0;
    const float b = 8.0 / 11.0;
    const float c = 9.0 / 10.0;

    const float ca = 4356.0 / 361.0;
    const float cb = 35442.0 / 1805.0;
    const float cc = 16061.0 / 1805.0;

    float t2 = t * t;

    return t < a
        ? 7.5625 * t2
        : t < b
            ? 9.075 * t2 - 9.9 * t + 3.4
            : t < c
                ? ca * t2 - cb * t + cc
                : 10.8 * t * t - 20.52 * t + 10.72;
}"""

    @ShaderPhrase
    val bounceIn = """#pragma import bounceOut
float bounceIn(in float t) {
    return 1.0 - bounceOut(1.0 - t);
}"""

    @ShaderPhrase
    val bounceInOut = """#pragma import bounceIn
#pragma import bounceOut
float bounceInOut(in float t) {
    return t < 0.5
        ? 0.5 * (1.0 - bounceOut(1.0 - t * 2.0))
        : 0.5 * bounceOut(t * 2.0 - 1.0) + 0.5;
}
    """.trimIndent()

    // https://github.com/patriciogonzalezvivo/lygia/blob/main/animation/easing/circular.glsl
    @ShaderPhrase
    val circularIn = """float circularIn(in float t) {
    return 1.0 - sqrt(1.0 - t * t);
}"""

    // https://github.com/patriciogonzalezvivo/lygia/blob/main/animation/easing/circular.glsl
    @ShaderPhrase
    val circularOut = """float circularOut(in float t) {
    return sqrt((2.0 - t) * t);
}"""

    // https://github.com/patriciogonzalezvivo/lygia/blob/main/animation/easing/circular.glsl
    @ShaderPhrase
    val circularInOut = """float circularInOut(in float t) {
    return t < 0.5
        ? 0.5 * (1.0 - sqrt(1.0 - 4.0 * t * t))
        : 0.5 * (sqrt((3.0 - 2.0 * t) * (2.0 * t - 1.0)) + 1.0);
}"""

    @ShaderPhrase
    val elasticIn = """#pragma import HALF_PI
float elasticIn(in float t) {
    return sin(13.0 * t * HALF_PI) * pow(2.0, 10.0 * (t - 1.0));
}"""

    @ShaderPhrase
    val elasticOut = """#pragma import HALF_PI
float elasticOut(in float t) {
    return sin(-13.0 * (t + 1.0) * HALF_PI) * pow(2.0, -10.0 * t) + 1.0;
}"""

    @ShaderPhrase
    val elasticInOut = """#pragma import HALF_PI
float elasticInOut(in float t) {
    return t < 0.5
        ? 0.5 * sin(+13.0 * HALF_PI * 2.0 * t) * pow(2.0, 10.0 * (2.0 * t - 1.0))
        : 0.5 * sin(-13.0 * HALF_PI * ((2.0 * t - 1.0) + 1.0)) * pow(2.0, -10.0 * (2.0 * t - 1.0)) + 1.0;
}"""

    @ShaderPhrase
    val exponentialIn = """float exponentialIn(in float t) {
    return t == 0.0 ? t : pow(2.0, 10.0 * (t - 1.0));
}"""

    @ShaderPhrase
    val exponentialOut = """float exponentialOut(in float t) {
    return t == 1.0 ? t : 1.0 - pow(2.0, -10.0 * t);
}"""

    @ShaderPhrase
    val exponentialInOut = """float exponentialInOut(in float t) {
    return t == 0.0 || t == 1.0
        ? t
        : t < 0.5
            ? +0.5 * pow(2.0, (20.0 * t) - 10.0)
            : -0.5 * pow(2.0, 10.0 - (t * 20.0)) + 1.0;
}"""
    @ShaderPhrase
    val quadraticIn = """float quadraticIn(in float t) {
    return t * t;
}"""

    @ShaderPhrase
    val quadraticOut = """float quadraticOut(in float t) {
    return -t * (t - 2.0);
}"""

    @ShaderPhrase
    val quadraticInOut = """float quadraticInOut(in float t) {
    float p = 2.0 * t * t;
    return t < 0.5 ? p : -p + (4.0 * t) - 1.0;
}"""

    @ShaderPhrase
    val quarticIn = """float quarticIn(in float t) {
    return pow(t, 4.0);
}"""

    @ShaderPhrase
    val quarticOut = """float quarticOut(in float t) {
    return pow(t - 1.0, 3.0) * (1.0 - t) + 1.0;
}"""

    @ShaderPhrase
    val quarticInOut = """float quarticInOut(in float t) {
    return t < 0.5
        ? +8.0 * pow(t, 4.0)
        : -8.0 * pow(t - 1.0, 4.0) + 1.0;
}"""
    @ShaderPhrase
    val quinticIn = """float quinticIn(in float t) {
    return pow(t, 5.0);
}"""

    @ShaderPhrase
    val quinticOut = """float quinticOut(in float t) {
    return 1.0 - (pow(t - 1.0, 5.0));
}"""

    @ShaderPhrase
    val quinticInOut = """float quinticInOut(in float t) {
    return t < 0.5
        ? +16.0 * pow(t, 5.0)
        : -0.5 * pow(2.0 * t - 2.0, 5.0) + 1.0;
}"""

    @ShaderPhrase
    val sineIn = """#pragma import HALF_PI
float sineIn(in float t) {
    return sin((t - 1.0) * HALF_PI) + 1.0;
}"""

    @ShaderPhrase
    val sineOut = """#pragma import HALF_PI
float sineOut(in float t) {
    return sin(t * HALF_PI);
}"""

    @ShaderPhrase
    val sineInOut = """#pragma import PI
float sineInOut(in float t) {
    return -0.5 * (cos(PI * t) - 1.0);
}"""


}