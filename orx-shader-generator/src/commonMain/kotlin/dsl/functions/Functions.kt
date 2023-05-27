package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
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
            generator.emitPreamble(sb.preamble)
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
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}($parameter0Type x__, $parameter1Type y__) { 
${sb.code.replace("$0", "x__").replace("$1", "y__").prependIndent("    ")}                    
    return ${resultSym.name.replace("$0", "x__").replace("$1", "y__")};
}"""
            )
            sb.pop()
        }

        operator fun getValue(
            any: Any?,
            property: KProperty<*>
        ): (Symbol<T0>, Symbol<T1>) -> Function2Symbol<T0, T1, R> {
            return { x, y -> Function2Symbol(p0 = x, p1 = y, function = "${property.name}($0, $1)", type = returnType) }
        }
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

    // Function3
    class Function3PropertyProvider<T0, T1, T2, R>(
        private val generator: Generator,
        private val parameter0Type: String,
        private val parameter1Type: String,
        private val parameter2Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function3Property<T0, T1, T2, R> =
            Function3Property(property.name, generator, parameter0Type, parameter1Type, parameter2Type, returnType, f)
    }
    class Function3Property<T0, T1, T2, R>(
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        parameter2Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Symbol<R>,
    ) {
        init {
            val sb = ShaderBuilder()
            sb.push()
            val resultSym = sb.f(symbol("$0", "dc"), symbol("$1", "dc"), symbol("$2", "dc"))
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}($parameter0Type x__, $parameter1Type y__, $parameter2Type z__) { 
${sb.code.replace("$0", "x__").replace("$1", "y__").replace("$2", "z__").prependIndent("    ")}                    
    return ${resultSym.name.replace("$0", "x__").replace("$1", "y__").replace("$2", "z__")};
}"""
            )
            sb.pop()
        }

        operator fun getValue(
            any: Any?,
            property: KProperty<*>
        ): (Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Function3Symbol<T0, T1, T2, R> {
            return { x, y, z ->
                Function3Symbol(
                    p0 = x,
                    p1 = y,
                    p2 = z,
                    function = "${property.name}($0, $1, $2)",
                    type = returnType
                )
            }
        }
    }

    class Function4PropertyProvider<T0, T1, T2, T3, R>(
        private val generator: Generator,
        private val parameter0Type: String,
        private val parameter1Type: String,
        private val parameter2Type: String,
        private val parameter3Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>, Symbol<T3>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function4Property<T0, T1, T2, T3, R> =
            Function4Property(
                property.name,
                generator,
                parameter0Type,
                parameter1Type,
                parameter2Type,
                parameter3Type,
                returnType,
                f
            )
    }

    // Function4

    class Function4Property<T0, T1, T2, T3, R>(
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        parameter2Type: String,
        parameter3Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>, Symbol<T3>) -> Symbol<R>,
    ) {
        init {
            val sb = ShaderBuilder()
            sb.push()
            val resultSym = sb.f(symbol("$0", "dc"), symbol("$1", "dc"), symbol("$2", "dc"), symbol("$3", "dc"))
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}($parameter0Type x__, $parameter1Type y__, $parameter2Type z__, $parameter3Type w__) { 
${
                    sb.code.replace("$0", "x__").replace("$1", "y__").replace("$2", "z__").replace("$3", "w__")
                        .prependIndent("    ")
                }                    
    return ${resultSym.name.replace("$0", "x__").replace("$1", "y__").replace("$2", "z__").replace("$3", "z__")};
}"""
            )
            sb.pop()
        }

        operator fun getValue(
            any: Any?,
            property: KProperty<*>
        ): (Symbol<T0>, Symbol<T1>, Symbol<T2>, Symbol<T3>) -> Function4Symbol<T0, T1, T2, T3, R> {
            return { x, y, z, w ->
                Function4Symbol(
                    p0 = x,
                    p1 = y,
                    p2 = z,
                    p3 = w,
                    function = "${property.name}($0, $1, $2, $3)",
                    type = returnType
                )
            }
        }
    }
}