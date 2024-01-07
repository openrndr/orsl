package org.openrndr.orsl.extension.noise.functions
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.UIntVector2
import org.openrndr.orsl.shadergenerator.dsl.UIntVector3
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.jvm.JvmName
import kotlin.math.PI

/**
 * commonly appears as lowbias32
 * https://nullprogram.com/blog/2018/07/31/
 */
fun ShaderBuilder.uhash11(x: Symbol<UInt>): Symbol<UInt> {
    val uhash11 by function<UInt, UInt>(false) {
        var a by variable(it)
        a = a xor (a shr 16)
        a *= 0x7feb352du
        a = a xor (a shr 15)
        a *= 0x846ca68bu
        a = a xor (a shr 16)
        a
    }
    return uhash11(x)
}

/**
 * set the seed value
 */
@JvmName("setSeedSui")
fun ShaderBuilder.setSeed(seed: Symbol<UInt>) {
    var uhashSeed by global<UInt>()
    uhashSeed = seed
}

@JvmName("setSeedSuiv2")
fun ShaderBuilder.setSeed(seed: Symbol<UIntVector2>) {
    var uhashSeed by global<UInt>()
    uhashSeed = uhash11(seed.x)
    uhashSeed = uhash11(uhashSeed + seed.y)
}


/**
 * set the seed value from [UIntVector3]
 */
@JvmName("setSeedSuiv3")
fun ShaderBuilder.setSeed(seed: Symbol<UIntVector3>) {
    var uhashSeed by global<UInt>()
    uhashSeed = uhash11(seed.x)
    uhashSeed = uhash11(uhashSeed + seed.y)
    uhashSeed = uhash11(uhashSeed + seed.z)
}

/**
 * @see setSeed
 */
fun ShaderBuilder.uniform1(): Symbol<Double> {
    var uhashSeed by global<UInt>()
    val uniform1 by function<Double>(false) {
        val s by uhash11(uhashSeed)
        uhashSeed = s
        s.double / 0xffffffffU.symbol.double
    }
    return uniform1()
}

/**
 * @see setSeed
 */
fun ShaderBuilder.uniform2(): Symbol<Vector2> {
    val uniform2 by function<Vector2>(false) {
        Vector2(uniform1(), uniform1())
    }
    return uniform2()
}

/**
 * @see setSeed
 */
fun ShaderBuilder.uniform3(): Symbol<Vector3> {
    val uniform3 by function<Vector3>(false) {
        Vector3(uniform1(), uniform1(), uniform1())
    }
    return uniform3()
}

/**
 * @see setSeed
 */
fun ShaderBuilder.uniform4(): Symbol<Vector4> {
    val uniform4 by function<Vector4>(false) {
        Vector4(uniform1(), uniform1(), uniform1(), uniform1())
    }
    return uniform4()
}


/**
 * @see setSeed
 */
fun ShaderBuilder.uniformDisk(): Symbol<Vector2> {
    val uniformDisk by function<Vector2>(false) {
        val r by uniform2()
        Vector2(sin(r.x * PI * 2.0), cos(r.x * PI * 2.0)) * sqrt(r.y)
    }
    return uniformDisk()
}

/**
 * @see setSeed
 */
fun ShaderBuilder.uniformSphere(): Symbol<Vector3> {
    val uniformSphere by function<Vector3>(false) {
        val r by uniform2()
        val theta by r.x * PI * 2.0
        val phi by acos(1.0 - 2.0 * r.y)
        Vector3(sin(phi) * cos(phi), sin(phi) * cos(theta), cos(phi))
    }
    return uniformSphere()
}


/**
 * @see setSeed
 * @see uniformSphere
 */
fun ShaderBuilder.uniformBall(): Symbol<Vector3> {
    val uniformBall by function<Vector3>(false) {
        val s by uniformSphere()
        val u by uniform1()
        s * pow(u, (1.0 / 3.0).symbol)
    }
    return uniformBall()
}
