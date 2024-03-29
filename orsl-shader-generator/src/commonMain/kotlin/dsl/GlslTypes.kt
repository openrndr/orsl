package org.openrndr.orsl.shadergenerator.dsl

import org.openrndr.color.ColorRGBa
import org.openrndr.math.*

inline fun <reified T> zero(): String {
    return when(T::class) {
        Boolean::class -> "false"
        Double::class -> "0.0"
        Int::class -> "0"
        Vector2::class -> "vec2(0.0)"
        IntVector2::class -> "ivec2(0)"
        Vector3::class -> "vec3(0.0)"
        IntVector3::class -> "ivec3(0)"
        Vector4::class -> "vec4(0.0)"
        IntVector4::class -> "ivec4(0)"
        else -> error("static zero not supported for '${T::class}")
    }
}

inline fun <reified T> one(): String {
    return when(T::class) {
        Boolean::class -> "true"
        Double::class -> "1.0"
        Int::class -> "1"
        Vector2::class -> "vec2(1.0)"
        IntVector2::class -> "ivec2(1)"
        Vector3::class -> "vec3(1.0)"
        IntVector3::class -> "ivec3(1)"
        Vector4::class -> "vec4(1.0)"
        IntVector4::class -> "ivec4(1)"
        else -> error("static one not supported for '${T::class}")
    }
}

inline fun <reified T> staticTypeOrNull(): String? {
    return when (T::class) {
        Boolean::class -> "bool"
        Double::class -> "float"
        Int::class -> "int"
        UInt::class -> "uint"
        Vector2::class -> "vec2"
        IntVector2::class -> "ivec2"
        UIntVector2::class -> "uvec2"
        Vector3::class -> "vec3"
        IntVector3::class -> "ivec3"
        UIntVector3::class -> "uvec3"
        Vector4::class -> "vec4"
        IntVector4::class -> "ivec4"
        Matrix33::class -> "mat3"
        Matrix44::class -> "mat4"
        Sampler2D::class -> "sampler2D"
        ColorRGBa::class -> "vec4"
        else -> T::class.simpleName
    }
}
fun dynamicTypeOrNull(v: Any): String? {
    return when(v) {
        is Boolean -> "bool"
        is Double -> "float"
        is Int -> "int"
        is UInt -> "uint"
        is Vector2 -> "vec2"
        is IntVector2 -> "ivec2"
        is UIntVector2 -> "uvec2"
        is Vector3 -> "vec3"
        is IntVector3 -> "ivec3"
        is Vector4 -> "vec4"
        is IntVector4 -> "ivec4"
        is Matrix33 -> "mat3"
        is Matrix44 -> "mat4"
        is Sampler2D -> "sampler2D"
        else -> null
    }
}

fun dynamicType(v: Any): String {
    return dynamicTypeOrNull(v) ?: error("dynamic type not supported ${v::class.simpleName}")
}

inline fun <reified T> staticType(): String {
    return staticTypeOrNull<T>() ?: error("static type not supported ${T::class.simpleName}")
}

fun glsl(v: Any?): String? {
    return when (v) {
        null -> null
        is Boolean -> if (v) "true" else "false"
        is Double -> "$v"
        is Int -> "$v"
        is UInt -> "${v}u"
        is Vector2 -> "vec2(${v.x}, ${v.y})"
        is IntVector2 -> "ivec2(${v.x}, ${v.y})"
        is Vector3 -> "vec3(${v.x}, ${v.y}, ${v.z})"
        is IntVector3 -> "ivec3(${v.x}, ${v.y}, ${v.z})"
        is Vector4 -> "vec4(${v.x}, ${v.y}, ${v.z}, ${v.w})"
        is IntVector4 -> "ivec4(${v.x}, ${v.y}, ${v.z}, ${v.w})"
        is ColorRGBa -> "vec4(${v.r}, ${v.g}, ${v.b}, ${v.alpha})"
        else -> error("type not supported ${v::class}")
    }
}
