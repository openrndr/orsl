package org.openrndr.extra.shadergenerator.dsl.structs

import org.openrndr.draw.Struct
import org.openrndr.extra.shadergenerator.dsl.ArraySymbol
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.activeGenerator
import org.openrndr.extra.shadergenerator.dsl.staticType
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

// this exists because Kotlin does not support local extension properties
operator fun <T: Struct<T>, F> KMutableProperty1<T, F>.setValue(
    any: Any?,
    property: KProperty<*>,
    symbol1: Symbol<F>
) {
    @Suppress("UNCHECKED_CAST")
    activeGenerator().emit("${((any as Symbol<F>).name)}.${this@setValue.name} = ${symbol1.name};")
}

inline operator fun <reified T:Struct<T>, reified F:Any> KProperty1<T, Array<F>>.getValue(any: Any?, kProperty: KProperty<*>) : ArraySymbol<F> {
    return object: ArraySymbol<F> {
        override val name: String
            get()  {
                @Suppress("UNCHECKED_CAST")
                return "${((any as Symbol<F>).name)}.${this@getValue.name}"
            }
        override val length: Int
            get() = TODO("Not yet implemented")
        override val type: String
            get() = staticType<F>()
    }
}


inline operator fun <reified T:Struct<T>, reified F:Any> KProperty1<T, F>.getValue(any: Any?, kProperty: KProperty<*>) : Symbol<F> {
    return object: Symbol<F> {
        override val name: String
            get()  {
                @Suppress("UNCHECKED_CAST")
                return "${((any as Symbol<F>).name)}.${this@getValue.name}"
            }
        override val type: String
            get() = staticType<F>()
    }
}

class ProtoArraySymbol<F>(val length: Int, val type: String) {
    operator fun getValue(any: Any?, kProperty: KProperty<*>) : ArraySymbol<F> {
        return object: ArraySymbol<F> {

            override val name: String
                get() = @Suppress("UNCHECKED_CAST") "${((any as Symbol<F>).name)}.${kProperty.name}"
            override val length: Int
                get() = this@ProtoArraySymbol.length
            override val type: String
                get() = this@ProtoArraySymbol.type

        }
    }
}
inline operator fun <reified T:Struct<T>,  reified F:Any> KProperty1<T, Array<F>>.get(length: Int) : ProtoArraySymbol<F> {
    return ProtoArraySymbol<F>(length, staticType<F>())
}

inline operator fun <reified T: Struct<T>> T.getValue(any: Any?, property: KProperty<*>) : Symbol<T> {
    return object: Symbol<T> {
        override val name: String
            get() = property.name
        override val type: String
            get() = staticType<T>()
    }
}
