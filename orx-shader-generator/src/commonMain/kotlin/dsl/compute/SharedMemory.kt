package org.openrndr.extra.shadergenerator.phrases.dsl.compute

import org.openrndr.extra.shadergenerator.compute.ComputeTransformBuilder
import org.openrndr.extra.shadergenerator.dsl.*
import kotlin.reflect.KProperty

class SharedMemoryVariableProperty<T>(val generator: Generator, val type:String) {
    operator fun provideDelegate(any: Any?, property: KProperty<*>) : SharedMemoryVariableProperty<T> {
        generator.emitPreamble("shared ${type} ${property.name};")
        return this
    }
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}

class SharedMemoryArrayVariableProperty<T>(val generator: Generator, val length: Int, val type:String) {
    operator fun provideDelegate(any: Any?, property: KProperty<*>) : SharedMemoryArrayVariableProperty<T> {
        generator.emitPreamble("shared ${type}[$length] ${property.name};")
        return this
    }

    operator fun getValue(any: Any?, property: KProperty<*>): ArraySymbol<T> {
        return object : ArraySymbol<T> {
            override val name: String
                get() = property.name
            override val length: Int = this@SharedMemoryArrayVariableProperty.length
            override val type = this@SharedMemoryArrayVariableProperty.type
        }
    }



    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}

inline fun <reified T> ComputeTransformBuilder.sharedMemoryVariable(): SharedMemoryVariableProperty<T> {
    val glslType = staticType<T>()
    return SharedMemoryVariableProperty(this@sharedMemoryVariable, glslType)
}

inline fun <reified T> ComputeTransformBuilder.sharedMemoryArray(length: Int): SharedMemoryArrayVariableProperty<T> {
    val glslType = staticType<T>()
    return SharedMemoryArrayVariableProperty(this@sharedMemoryArray, length, glslType)
}