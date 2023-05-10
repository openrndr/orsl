package org.openrndr.extra.shadergenerator.phrases.dsl

import kotlin.reflect.KProperty

class OutputProperty<T>(val generator: Generator) {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        generator.emit("${property.name} = ${value.name};")
    }
}

class ConstantProperty<T> {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name)
}

class VariableProperty<T>(val generator: Generator, val glslType:String) {
    private var declarationEmitted = false
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        if (!declarationEmitted) {
            generator.emit("${glslType} ${property.name};")
            declarationEmitted = true
        }
        generator.emit("${property.name} = ${value.name};")
    }
}

class ArrayVariableProperty<T>(val generator: Generator, val length: Int, val glslType:String) {
    private var declarationEmitted = false
    operator fun getValue(any: Any?, property: KProperty<*>): ArraySymbol<T> {
        if (!declarationEmitted) {
            generator.emit("${glslType} ${property.name}[$length];")
            declarationEmitted = true
        }
        return object : ArraySymbol<T> {
            override val name: String
                get() = property.name
            override val length: Int = this@ArrayVariableProperty.length
            override val type = glslType
        }
    }
    operator fun setValue(any: Any?, property: KProperty<*>, value: ArraySymbol<T>) {
        generator.emit("${property.name} = ${value.name};")
    }

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        if (!declarationEmitted) {
            generator.emit("${glslType} ${property.name};")
            declarationEmitted = true
        }
        generator.emit("${property.name} = ${value.name};")
    }
}

class ValueProperty<T> {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name)
}

class ArrayValueProperty<T>(
    val generator: Generator, val length: Int, val glslType:String) {
    operator fun getValue(any: Any?, property: KProperty<*>): ArraySymbol<T> {
        return object : ArraySymbol<T> {
            override val name: String
                get() = property.name
            override val length: Int = this@ArrayValueProperty.length
            override val type = glslType
        }
    }
}

