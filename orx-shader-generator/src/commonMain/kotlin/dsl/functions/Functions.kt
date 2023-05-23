package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.math.*
import kotlin.reflect.KProperty

@Suppress("INAPPLICABLE_JVM_NAME")
interface Functions {

    class FunctionProperty<T, R>(
        name: String,
        generator: Generator,
        parameter0Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T>) -> Symbol<R>,
    ) {
        init {
            val sb = ShaderBuilder()
            sb.push()
            val resultSym = sb.f(symbol("$0", "dc"))
            generator.emitPreamble(
                """$returnType ${name}($parameter0Type x__) { 
${sb.code.replace("$0", "x__").prependIndent("    ")}                    
    return ${resultSym.name.replace("$0", "x__")};
}"""
            )
            sb.pop()
        }
        operator fun getValue(any: Any?, property: KProperty<*>): (Symbol<T>) -> FunctionSymbol1<T, R> {
            return { x -> FunctionSymbol1(p0 = x, function = "${property.name}($0)", type = returnType) }
        }
    }

    class FunctionPropertyProvider<T, R>(
        private val generator: Generator,
        private val parameter0Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): FunctionProperty<T, R> =
            FunctionProperty(property.name, generator, parameter0Type, returnType, f)
    }


    class Function2PropertyProvider<T0, T1, R>(
        private val generator: Generator,
        private val parameter0Type: String,
        private val parameter1Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T0>, Symbol<T1>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function2Property<T0, T1, R> =
            Function2Property(property.name, generator, parameter0Type, parameter1Type, returnType, f)
    }

    class Function2Property<T0, T1, R>(
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>) -> Symbol<R>,
    ) {
        init {
            val sb = ShaderBuilder()
            sb.push()
            val resultSym = sb.f(symbol("$0", "dc"), symbol("$1", "dc"))
            generator.emitPreamble(
                """$returnType ${name}($parameter0Type x__, $parameter1Type y__) { 
${sb.code.replace("$0", "x__").replace("$1", "y__").prependIndent("    ")}                    
    return ${resultSym.name.replace("$0", "x__").replace("$1", "y__")};
}"""
            )
            sb.pop()
        }
        operator fun getValue(any: Any?, property: KProperty<*>): (Symbol<T0>, Symbol<T1>) -> Function2Symbol<T0, T1, R> {
            return { x, y -> Function2Symbol(p0 = x, p1 = y, function = "${property.name}($0, $1)", type = returnType) }
        }
    }


    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Symbol<Double>) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, ${w.name})"
        override val type: String = "vec4"
    }

    fun Vector4(x: Symbol<Double>, y: Symbol<Double>, z: Symbol<Double>, w: Double) = object : Symbol<Vector4> {
        override val name: String = "vec4(${x.name}, ${y.name}, ${z.name}, $w)"
        override val type: String = "vec4"
    }

}