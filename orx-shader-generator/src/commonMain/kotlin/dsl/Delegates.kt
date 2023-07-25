package org.openrndr.extra.shadergenerator.dsl

import kotlin.reflect.KProperty

class OutputProperty<T>(val generator: Generator, val type:String) {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}


class FragmentOutputProperty<T>(val generator: Generator, val type:String) {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}

class ConstantProperty<T>(val type: String) {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)
}

class GlobalProperty<T>(val generator: Generator, val type:String) {
    operator fun provideDelegate(any: Any?, property: KProperty<*>) : GlobalProperty<T> {
        generator.emitPreamble(property.name, "${type} ${property.name};")
        return this
    }
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}


class VariableProperty<T>(val generator: Generator, val type:String,
                          val initialValue: T?, val initialSymbol: Symbol<T>?) {
    operator fun provideDelegate(any: Any?, property: KProperty<*>) : VariableProperty<T> {
        if (initialValue == null && initialSymbol == null) {
            generator.emit("${type} ${property.name};")
        } else if (initialValue != null) {
            generator.emit("${type} ${property.name} = ${glsl(initialValue)};")
        } else if (initialSymbol != null) {
            generator.emit("${type} ${property.name} = ${initialSymbol.name};")
        }

        return this
    }
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)

    operator fun setValue(any: Any?, property: KProperty<*>, value: Symbol<T>) {
        activeGenerator().emit("${property.name} = ${value.name};")
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
        activeGenerator().emit("${property.name} = ${value.name};")
    }
}

class ValueProperty<T>(val type:String) {
    operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> = symbol(property.name, type)
}

class ArrayValueProperty<T>(
    val generator: Generator, val length: Int, val type:String) {

    operator fun provideDelegate(any: Any?, property: KProperty<*>) : ArrayValueProperty<T> {
        generator.emit("$type ${property.name}[$length];")
        return this
    }
    operator fun getValue(any: Any?, property: KProperty<*>): ArraySymbol<T> {
        return object : ArraySymbol<T> {
            override val name: String
                get() = property.name
            override val length: Int = this@ArrayValueProperty.length
            override val type = this@ArrayValueProperty.type
        }
    }
}

