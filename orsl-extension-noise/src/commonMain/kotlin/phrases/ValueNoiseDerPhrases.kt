package org.openrndr.orsl.extension.noise.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase
import org.openrndr.orsl.shadergenerator.annotations.WrapShaderBook

//Based on:
// * http://www.iquilezles.org/www/articles/gradientnoise/gradientnoise.htm
// * https://github.com/patriciogonzalezvivo/lygia/blob/main/generative/noised.glsl
// uses signed hash noise instead of using sin/fract noise
@IndexShaderBook
@WrapShaderBook(extensionFunctions = true)
class ValueNoiseDerPhrases : ShaderBook {

    @ShaderPhrase
    val value12D = """#pragma import shash22
vec3 value12D(in vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    // quintic interpolation
    vec2 u = f * f * f * (f * (f * 6.0 - 15.0) + 10.0);
    vec2 du = 30.0 * f * f * (f * (f - 2.0) + 1.0);

    vec2 ga = shash22(i + vec2(0.0, 0.0));
    vec2 gb = shash22(i + vec2(1.0, 0.0));
    vec2 gc = shash22(i + vec2(0.0, 1.0));
    vec2 gd = shash22(i + vec2(1.0, 1.0));

    float va = dot(ga, f - vec2(0.0, 0.0));
    float vb = dot(gb, f - vec2(1.0, 0.0));
    float vc = dot(gc, f - vec2(0.0, 1.0));
    float vd = dot(gd, f - vec2(1.0, 1.0));

    return vec3( va + u.x*(vb-va) + u.y*(vc-va) + u.x*u.y*(va-vb-vc+vd),   // value
                ga + u.x*(gb-ga) + u.y*(gc-ga) + u.x*u.y*(ga-gb-gc+gd) +  // derivatives
                du * (u.yx*(va-vb-vc+vd) + vec2(vb,vc) - va));
}"""

    @ShaderPhrase
    val value12 = """#pragma import shash22
float value12(in vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    // quintic interpolation
    vec2 u = f * f * f * (f * (f * 6.0 - 15.0) + 10.0);
    vec2 du = 30.0 * f * f * (f * (f - 2.0) + 1.0);

    vec2 ga = shash22(i + vec2(0.0, 0.0));
    vec2 gb = shash22(i + vec2(1.0, 0.0));
    vec2 gc = shash22(i + vec2(0.0, 1.0));
    vec2 gd = shash22(i + vec2(1.0, 1.0));

    float va = dot(ga, f - vec2(0.0, 0.0));
    float vb = dot(gb, f - vec2(1.0, 0.0));
    float vc = dot(gc, f - vec2(0.0, 1.0));
    float vd = dot(gd, f - vec2(1.0, 1.0));

    return va + u.x*(vb-va) + u.y*(vc-va) + u.x*u.y*(va-vb-vc+vd);
}"""

    @ShaderPhrase
    val value13D = """#pragma import shash33
vec4 value13D(in vec3 pos) {
    vec3 p = floor(pos);
    vec3 w = fract(pos);

    // quintic interpolant
    vec3 u = w * w * w * ( w * (w * 6. - 15.) + 10. );
    vec3 du = 30.0 * w * w * ( w * (w - 2.) + 1.);

    // gradients
    vec3 ga = shash33(p + vec3(0., 0., 0.));
    vec3 gb = shash33(p + vec3(1., 0., 0.));
    vec3 gc = shash33(p + vec3(0., 1., 0.));
    vec3 gd = shash33(p + vec3(1., 1., 0.));
    vec3 ge = shash33(p + vec3(0., 0., 1.));
    vec3 gf = shash33(p + vec3(1., 0., 1.));
    vec3 gg = shash33(p + vec3(0., 1., 1.));
    vec3 gh = shash33(p + vec3(1., 1., 1.));

    // projections
    float va = dot(ga, w - vec3(0., 0., 0.));
    float vb = dot(gb, w - vec3(1., 0., 0.));
    float vc = dot(gc, w - vec3(0., 1., 0.));
    float vd = dot(gd, w - vec3(1., 1., 0.));
    float ve = dot(ge, w - vec3(0., 0., 1.));
    float vf = dot(gf, w - vec3(1., 0., 1.));
    float vg = dot(gg, w - vec3(0., 1., 1.));
    float vh = dot(gh, w - vec3(1., 1., 1.));

    // interpolations
    return vec4( va + u.x*(vb-va) + u.y*(vc-va) + u.z*(ve-va) + u.x*u.y*(va-vb-vc+vd) + u.y*u.z*(va-vc-ve+vg) + u.z*u.x*(va-vb-ve+vf) + (-va+vb+vc-vd+ve-vf-vg+vh)*u.x*u.y*u.z,    // value
                ga + u.x*(gb-ga) + u.y*(gc-ga) + u.z*(ge-ga) + u.x*u.y*(ga-gb-gc+gd) + u.y*u.z*(ga-gc-ge+gg) + u.z*u.x*(ga-gb-ge+gf) + (-ga+gb+gc-gd+ge-gf-gg+gh)*u.x*u.y*u.z +   // derivatives
                du * (vec3(vb,vc,ve) - va + u.yzx*vec3(va-vb-vc+vd,va-vc-ve+vg,va-vb-ve+vf) + u.zxy*vec3(va-vb-ve+vf,va-vb-vc+vd,va-vc-ve+vg) + u.yzx*u.zxy*(-va+vb+vc-vd+ve-vf-vg+vh) ));
}"""

    @ShaderPhrase
    val value13 = """#pragma import shash33
float value13(in vec3 pos) {
    vec3 p = floor(pos);
    vec3 w = fract(pos);

    // quintic interpolant
    vec3 u = w * w * w * ( w * (w * 6. - 15.) + 10. );
    vec3 du = 30.0 * w * w * ( w * (w - 2.) + 1.);

    // gradients
    vec3 ga = shash33(p + vec3(0., 0., 0.));
    vec3 gb = shash33(p + vec3(1., 0., 0.));
    vec3 gc = shash33(p + vec3(0., 1., 0.));
    vec3 gd = shash33(p + vec3(1., 1., 0.));
    vec3 ge = shash33(p + vec3(0., 0., 1.));
    vec3 gf = shash33(p + vec3(1., 0., 1.));
    vec3 gg = shash33(p + vec3(0., 1., 1.));
    vec3 gh = shash33(p + vec3(1., 1., 1.));

    // projections
    float va = dot(ga, w - vec3(0., 0., 0.));
    float vb = dot(gb, w - vec3(1., 0., 0.));
    float vc = dot(gc, w - vec3(0., 1., 0.));
    float vd = dot(gd, w - vec3(1., 1., 0.));
    float ve = dot(ge, w - vec3(0., 0., 1.));
    float vf = dot(gf, w - vec3(1., 0., 1.));
    float vg = dot(gg, w - vec3(0., 1., 1.));
    float vh = dot(gh, w - vec3(1., 1., 1.));

    // interpolations
    return va + u.x*(vb-va) + u.y*(vc-va) + u.z*(ve-va) + u.x*u.y*(va-vb-vc+vd) + u.y*u.z*(va-vc-ve+vg) + u.z*u.x*(va-vb-ve+vf) + (-va+vb+vc-vd+ve-vf-vg+vh)*u.x*u.y*u.z;

}"""
}