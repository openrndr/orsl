package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.phrases.dsl.*
import org.openrndr.math.*
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface Functions {



    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Symbol<Double>) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, ${w.name})"
        override val type: String = "vec4"
    }

    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Double) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, $w)"
        override val type: String = "vec4"
    }

}