package org.openrndr.extra.shadergenerator.phrases.dsl

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

inline fun <reified T> staticType(): String {
    return when (T::class) {
        Boolean::class -> "bool"
        Double::class -> "float"
        Int::class -> "int"
        Vector2::class -> "vec2"
        IntVector2::class -> "ivec2"
        Vector3::class -> "vec3"
        IntVector3::class -> "ivec3"
        Vector4::class -> "vec4"
        IntVector4::class -> "ivec4"
        Matrix33::class -> "mat3"
        Matrix44::class -> "mat4"
        Sampler2D::class -> "sampler2D"
        else -> error("not supported")
    }
}

fun glsl(v: Any?): String? {
    return when (v) {
        null -> null
        is Boolean -> if (v) "true" else "false"
        is Double -> "$v"
        is Int -> "$v"
        is Vector2 -> "vec2(${v.x}, ${v.y})"
        is IntVector2 -> "ivec2(${v.x}, ${v.y})"
        is Vector3 -> "vec3(${v.x}, ${v.y}, ${v.z})"
        is IntVector3 -> "ivec3(${v.x}, ${v.y}, ${v.z})"
        is Vector4 -> "vec4(${v.x}, ${v.y}, ${v.z}, ${v.w})"
        is IntVector4 -> "ivec4(${v.x}, ${v.y}, ${v.z}, ${v.w})"
        else -> error("type not supported ${v::class}")
    }
}
