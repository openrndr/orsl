package org.openrndr.orsl.shadergenerator.phrases.dsl.functions

import org.openrndr.orsl.shadergenerator.dsl.Generator

interface MemoryBarrierFunctions : Generator {
    /**
     * controls the ordering of memory transaction issued shader invocation relative to a work group
     *
     * [groupMemoryBarrier] waits on the completion of all memory accesses performed by an invocation of a compute shader relative to the same access performed by other invocations in the same work group and then returns with no other effect.
     */
    fun groupMemoryBarrier() {
        emit("groupMemoryBarrier();")
    }

    /**
     * controls the ordering of memory transactions issued by a single shader invocation
     *
     * [memoryBarrier] waits on the completion of all memory accesses resulting from the use of image variables or atomic counters and then returns with no other effect. When this function returns, the results of any memory stores performed using coherent variables performed prior to the call will be visible to any future coherent memory access to the same addresses from other shader invocations. In particular, the values written this way in one shader stage are guaranteed to be visible to coherent memory accesses performed by shader invocations in subsequent stages when those invocations were triggered by the execution of the original shader invocation (e.g., fragment shader invocations for a primitive resulting from a particular geometry shader invocation).
     */
    fun memoryBarrier() {
        emit("memoryBarrier();")
    }

    /**
     * controls the ordering of operations on atomic counters issued by a single shader invocation
     *
     * [memoryBarrierAtomicCounter] waits on the completion of all accesses resulting from the use of atomic counters and then returns with no other effect. When this function returns, the results of any modifications to the value of atomic counters will be visible to any access to the same counter from other shader invocations. In particular, any modifications made in one shader stage are guaranteed to be visible to accesses performed by shader invocations in subsequent stages when those invocations were triggered by the execution of the original shader invocation (e.g., fragment shader invo
     */
    fun memoryBarrierAtomicCounter() {
        emit("memoryBarrierAtomicCounter();")
    }

    /**
     * controls the ordering of operations on buffer variables issued by a single shader invocation
     *
     * memoryBarrierBuffer waits on the completion of all memory accesses resulting from the use of buffer variables and then returns with no other effect. When this function returns, the results of any modifications to the content of buffer variables will be visible to any access to the same buffer from other shader invocations. In particular, any modifications made in one shader stage are guaranteed to be visible to accesses performed by shader invocations in subsequent stages when those invocations were triggered by the execution of the original shader invocation (e.g., fragment shader invocations for a primitive resulting from a particular geometry shader invocation).
     */
    fun memoryBarrierBuffer() {
        emit("memoryBarrierBuffer();")
    }


    /**
     * controls the ordering of operations on image variables issued by a single shader invocation
     *
     * [memoryBarrierImage] waits on the completion of all memory accesses resulting from the use of image variables and then returns with no other effect. When this function returns, the results of any modifications to the content of image variables will be visible to any access to the same buffer from other shader invocations. In particular, any modifications made in one shader stage are guaranteed to be visible to accesses performed by shader invocations in subsequent stages when those invocations were triggered by the execution of the original shader invocation (e.g., fragment shader invocations for a primitive resulting from a particular geometry shader invocation).
     */
    fun memoryBarrierImage() {
        emit("memoryBarrierImage();")
    }

    /**
     * controls the ordering of operations on shared variables issued by a single shader invocation
     *
     * [memoryBarrierShared] waits on the completion of all memory accesses resulting from the use of shared variables and then returns with no other effect. When this function returns, the results of any modifications to the content of shared variables will be visible to any access to the same buffer from other shader invocations. In particular, any modifications made in one shader stage are guaranteed to be visible to accesses performed by shader invocations in subsequent stages when those invocations were triggered by the execution of the original shader invocation (e.g., fragment shader invocations for a primitive resulting from a particular geometry shader invocation).
     */
    fun memoryBarrierShared() {
        emit("memoryBarrierShared();")
    }


}