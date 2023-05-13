package org.openrndr.extra.shadergenerator.phrases.dsl.structs

import org.openrndr.extra.shadergenerator.phrases.dsl.*
import kotlin.reflect.KProperty

class FieldSymbol<T>(val name: String, val type: String) {
}

abstract class Struct<T : Struct<T>>(val generator: Generator) {


    companion object {

    }

    abstract fun create() : T


    val fields = mutableMapOf<String, FieldSymbol<Any>>()

    var hackName = ""
    var hackType = ""

    operator fun provideDelegate(any: Any?, property: KProperty<*>): Struct<T> {
        println("here I make the struct type ${property.name}")
        hackType = property.name
        generator.emitPreamble(
            """struct ${property.name} {
${fields.values.joinToString("\n") { "${it.type} ${it.name};" }.prependIndent("    ")}
};"""
        )
        return this
    }

    operator fun getValue(any: T?, property: KProperty<*>): StructSymbol<T> {
        @Suppress("UNCHECKED_CAST")
        return StructSymbol(generator, property.name, create().apply { hackName= property.name })
    }

    inline fun <reified F> field(): Field<F> = Field(staticType<F>())

    inner class Field<F>(val type: String) {
        operator fun provideDelegate(any: Any, kProperty: KProperty<*>): Field<F> {
            fields[kProperty.name] = FieldSymbol(kProperty.name, type)
            return this
        }

        operator fun getValue(any: Any?, kProperty: KProperty<*>): Symbol<F> {
            return symbol("${hackName}.${kProperty.name}", type)
        }

        operator fun setValue(any: Any?, kProperty: KProperty<*>, value: Symbol<F>) {
            generator.emit("${hackName}.${kProperty.name} = ${value.name};")
        }
    }
}

data class StructSymbol< T : Struct<T>>(val generator: Generator, var name: String, val proto: T) {
    fun array(size: Int):ArrayValueProperty<T> {
        return ArrayValueProperty(generator, size, name)
    }
    operator fun provideDelegate(any: Any?, kProperty: KProperty<*>): StructSymbol<T> {
        println("setting hackname to ${kProperty.name}")
        proto.hackName = kProperty.name
        generator.emit("$name ${kProperty.name};")
        println("here we make a value ${kProperty.name} of type $name")
        return this
    }

    operator fun getValue(any: Any?, kProperty: KProperty<*>): T {
        return proto
    }
}
