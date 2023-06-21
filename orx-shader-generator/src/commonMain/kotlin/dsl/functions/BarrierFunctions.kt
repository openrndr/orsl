package org.openrndr.extra.shadergenerator.phrases.dsl.functions

import org.openrndr.extra.shadergenerator.compute.ComputeTransformBuilder
import org.openrndr.extra.shadergenerator.dsl.Generator

interface BarrierFunctions: Generator {
    fun bufferMemoryBarrier(f: ComputeTransformBuilder.() -> Unit) {
        val sb = ComputeTransformBuilder()
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """{
${sb.code.prependIndent("    ").trimEnd()}
    memoryBarrierBuffer();
}"""
        )
    }

    fun imageMemoryBarrier(f: ComputeTransformBuilder.() -> Unit) {
        val sb = ComputeTransformBuilder()
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """{
${sb.code.prependIndent("    ").trimEnd()}
    memoryBarrierImage();
}"""
        )
    }

    fun atomicCounterMemoryBarrier(f: ComputeTransformBuilder.() -> Unit) {
        val sb = ComputeTransformBuilder()
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """{
${sb.code.prependIndent("    ").trimEnd()}
    memoryBarrierAtomicCounter();
}"""
        )
    }
}