package org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName
import kotlin.math.PI

@JvmName("setBNMask2Sv2")
fun ShaderBuilder.setBNMask2(mask: Symbol<Vector2>) {
    var bnMask2 by global<Vector2>()
    bnMask2 = mask
}

fun ShaderBuilder.bnUniform2(): Symbol<Vector2> {
    val bnUniform2 by function<Vector2>(false) {
        val bnMask2 by global<Vector2>()
        (bnMask2 + Vector2(uniform1(), uniform1())).mod(Vector2(1.0, 1.0))
    }
    return bnUniform2()
}

fun ShaderBuilder.bnSphere(): Symbol<Vector3> {
    val bnSphere by function<Vector3>(false) {
        val r by bnUniform2()
        val theta by r.x * PI * 2.0
        val phi by acos(1.0 - 2.0 * r.y)
        Vector3(sin(phi) * cos(phi), sin(phi) * cos(theta), cos(phi))
    }
    return bnSphere()
}

fun ShaderBuilder.bnDisk(): Symbol<Vector2> {
    val uniformDisk by function<Vector2>(false) {
        val r by bnUniform2()
        Vector2(sin(r.x * PI * 2.0), cos(r.x * PI * 2.0)) * sqrt(r.y)
    }
    return uniformDisk()
}

fun ShaderBuilder.bnHemisphere(normal: Symbol<Vector3>): Symbol<Vector3> {
    val bnHemisphere by function<Vector3, Vector3> { normal ->

        val r by bnUniform2()
        val u by r.x * 2.0 - 1.0
        val v by r.y
        val a by PI * v * 2.0

        val tn by normal + Vector3(Vector2(cos(a), sin(a)) * sqrt(1.0 - u * u), u)
        tn.normalized
    }
    return bnHemisphere(normal)
}