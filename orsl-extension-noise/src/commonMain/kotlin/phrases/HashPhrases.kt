package org.openrndr.orsl.extension.noise.phrases

import org.openrndr.orsl.shadergenerator.annotations.IndexShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderBook
import org.openrndr.orsl.shadergenerator.annotations.ShaderPhrase
import org.openrndr.orsl.shadergenerator.annotations.WrapShaderBook

@IndexShaderBook
@WrapShaderBook(extensionFunctions = true)
class HashPhrases : ShaderBook {
    /** https://www.shadertoy.com/view/4djSRW */
    @ShaderPhrase
    val hash11 = """float hash11(float p) {
    p = fract(p * .1031);
    p *= p + 33.33;
    p *= p + p;
    return fract(p);
}"""

    @ShaderPhrase
    val shash11 = """float shash11(float p) {
    p = fract(p * .1031);
    p *= p + 33.33;
    p *= p + p;
    return fract(p) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash12 = """float hash12(vec2 p) {
	vec3 p3  = fract(vec3(p.xyx) * .1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}"""
    @ShaderPhrase
    val shash12 = """float shash12(vec2 p) {
	vec3 p3  = fract(vec3(p.xyx) * .1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash13 = """float hash13(vec3 p3) {
	p3  = fract(p3 * .1031);
    p3 += dot(p3, p3.zyx + 31.32);
    return fract((p3.x + p3.y) * p3.z);
}"""

    @ShaderPhrase
    val shash13 = """float shash13(vec3 p3) {
	p3  = fract(p3 * .1031);
    p3 += dot(p3, p3.zyx + 31.32);
    return fract((p3.x + p3.y) * p3.z) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash14 = """ float hash14(vec4 p4) {
	p4 = fract(p4  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.x + p4.y) * (p4.z + p4.w));
}"""

    @ShaderPhrase
    val shash14 = """ float shash14(vec4 p4) {
	p4 = fract(p4  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.x + p4.y) * (p4.z + p4.w)) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash21 = """vec2 hash21(float p) {
	vec3 p3 = fract(vec3(p) * vec3(.1031, .1030, .0973));
	p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}"""

    @ShaderPhrase
    val shash21 = """vec2 shash21(float p) {
	vec3 p3 = fract(vec3(p) * vec3(.1031, .1030, .0973));
	p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx+p3.yz)*p3.zy) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash22 = """vec2 hash22(vec2 p) {
	vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}"""

    @ShaderPhrase
    val shash22 = """vec2 shash22(vec2 p) {
	vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash23 = """vec2 hash23(vec3 p3) {
    p3 = fract(p3 * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy);
}"""

    @ShaderPhrase
    val shash23 = """vec2 shash23(vec3 p3) {
    p3 = fract(p3 * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xx+p3.yz)*p3.zy) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash31 = """vec3 hash31(float p) {
    vec3 p3 = fract(vec3(p) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xxy+p3.yzz)*p3.zyx); 
}"""

    @ShaderPhrase
    val shash31 = """vec3 shash31(float p) {
    vec3 p3 = fract(vec3(p) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yzx+33.33);
    return fract((p3.xxy+p3.yzz)*p3.zyx) * 2.0 - 1.0; 
}"""

    @ShaderPhrase
    val hash32 = """vec3 hash32(vec2 p) {
	vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yxz+33.33);
    return fract((p3.xxy+p3.yzz)*p3.zyx);
}"""

    @ShaderPhrase
    val shash32 = """vec3 shash32(vec2 p) {
	vec3 p3 = fract(vec3(p.xyx) * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yxz+33.33);
    return fract((p3.xxy+p3.yzz)*p3.zyx) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash33 = """vec3 hash33(vec3 p3) {
	p3 = fract(p3 * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yxz+33.33);
    return fract((p3.xxy + p3.yxx)*p3.zyx);
}"""

    @ShaderPhrase
    val shash33 = """vec3 shash33(vec3 p3) {
	p3 = fract(p3 * vec3(.1031, .1030, .0973));
    p3 += dot(p3, p3.yxz+33.33);
    return fract((p3.xxy + p3.yxx)*p3.zyx);
}"""

    @ShaderPhrase
    val hash41 = """vec4 hash41(float p) {
	vec4 p4 = fract(vec4(p) * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}"""

    @ShaderPhrase
    val shash41 = """vec4 shash41(float p) {
	vec4 p4 = fract(vec4(p) * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}"""

    @ShaderPhrase
    val hash42 = """vec4 hash42(vec2 p) {
	vec4 p4 = fract(vec4(p.xyxy) * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}"""

    @ShaderPhrase
    val shash42 = """vec4 shash42(vec2 p) {
	vec4 p4 = fract(vec4(p.xyxy) * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash43 = """vec4 hash43(vec3 p) {
	vec4 p4 = fract(vec4(p.xyzx)  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}"""

    @ShaderPhrase
    val shash43 = """vec4 shash43(vec3 p) {
	vec4 p4 = fract(vec4(p.xyzx)  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx) * 2.0 - 1.0;
}"""

    @ShaderPhrase
    val hash44 = """vec4 hash44(vec4 p4) {
	p4 = fract(p4  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx);
}"""

    @ShaderPhrase
    val shash44 = """vec4 shash44(vec4 p4) {
	p4 = fract(p4  * vec4(.1031, .1030, .0973, .1099));
    p4 += dot(p4, p4.wzxy+33.33);
    return fract((p4.xxyz+p4.yzzw)*p4.zywx) * 2.0 - 1.0;
}"""
}