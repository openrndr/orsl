package org.openrndr.orsl.shadergenerator.phrases.dsl.compute

import org.openrndr.orsl.shadergenerator.compute.ComputeTransformBuilder
import org.openrndr.orsl.shadergenerator.dsl.*

interface ComputeFunctions : Generator {
}

fun ComputeTransformBuilder.return_() {
    activeGenerator().emit("return;")
}

fun ComputeTransformBuilder.sharedMemoryBarrier(f: ComputeTransformBuilder.() -> Unit) {
    val sb = ComputeTransformBuilder()
    sb.push()
    sb.f()
    sb.pop()
    emitPreamble(sb.preamble)
    emit(
        """{
${sb.code.prependIndent("    ").trimEnd()}
    memoryBarrierShared();
}"""
    )
}