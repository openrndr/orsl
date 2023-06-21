package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.*
import kotlin.reflect.KProperty


inline fun <reified R> Generator.function(useHash: Boolean = true, noinline f: ShaderBuilder.() -> Symbol<R>): Functions.Function0PropertyProvider<R> =
    Functions.Function0PropertyProvider(
        useHash,
        this,
        returnType = staticType<R>(),
        f
    )


inline fun <reified T, reified R> Generator.function(useHash: Boolean = true, noinline f: ShaderBuilder.(Symbol<T>) -> Symbol<R>): Functions.FunctionPropertyProvider<T, R> =
    Functions.FunctionPropertyProvider(
        useHash,
        this,
        parameter0Type = staticType<T>(),
        returnType = staticType<R>(),
        f
    )

inline fun <reified T0, reified T1, reified R> Generator.function(useHash: Boolean = true, noinline f: ShaderBuilder.(Symbol<T0>, Symbol<T1>) -> Symbol<R>): Functions.Function2PropertyProvider<T0, T1, R> =
    Functions.Function2PropertyProvider(
        useHash,
        this,
        parameter0Type = staticType<T0>(),
        parameter1Type = staticType<T1>(),
        returnType = staticType<R>(),
        f
    )

inline fun <reified T0, reified T1, reified T2, reified R> Generator.function(useHash: Boolean = true,noinline f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Symbol<R>): Functions.Function3PropertyProvider<T0, T1, T2, R> =
    Functions.Function3PropertyProvider(
        useHash,
        this,
        parameter0Type = staticType<T0>(),
        parameter1Type = staticType<T1>(),
        parameter2Type = staticType<T2>(),
        returnType = staticType<R>(),
        f
    )

inline fun <reified T0, reified T1, reified T2, reified T3, reified R> Generator.function(useHash: Boolean = true, noinline f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>, Symbol<T3>) -> Symbol<R>): Functions.Function4PropertyProvider<T0, T1, T2, T3, R> =
    Functions.Function4PropertyProvider(
        useHash,
        this,
        parameter0Type = staticType<T0>(),
        parameter1Type = staticType<T1>(),
        parameter2Type = staticType<T2>(),
        parameter3Type = staticType<T3>(),
        returnType = staticType<R>(),
        f
    )


@Suppress("INAPPLICABLE_JVM_NAME")
interface Functions {
    class FunctionProperty<T, R>(
        useHash: Boolean,
        name: String,
        generator: Generator,
        parameter0Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T>) -> Symbol<R>,
    ) {
        private var functionHash = 0U

        init {
            val sb = ShaderBuilder()
            sb.push()
            val hash = hash(name, parameter0Type)
            val p0 = "$,,${hash}_0"
            val resultSym = sb.f(symbol(p0, parameter0Type))
            functionHash = if (useHash)
                hash(name, returnType, resultSym.name, resultSym.type, sb.code, sb.preamble, sb.tempId)
            else 0U

            if (useHash) {
                generator.emitPreamble("#ifndef f_${name}_${functionHash}")
                generator.emitPreamble("#define f_${name}_${functionHash}")
            }
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}_${functionHash}($parameter0Type x__) { 
${sb.code.replace(p0, "x__").prependIndent("    ").trimEnd()}                    
    return ${resultSym.name.replace(p0, "x__")};
}"""
            )
            if (useHash) {
                generator.emitPreamble("#endif")
            }
            sb.pop()
        }

        operator fun getValue(any: Any?, property: KProperty<*>): (Symbol<T>) -> FunctionSymbol1<T, R> {
            return { x ->
                FunctionSymbol1(
                    p0 = x,
                    function = "${property.name}_${functionHash}($0)",
                    type = returnType
                )
            }
        }
    }

    class Function0Property<R>(
        useHash: Boolean,
        name: String,
        generator: Generator,
        private val returnType: String,
        f: ShaderBuilder.() -> Symbol<R>,
    ) {
        private var functionHash = 0U

        init {
            val sb = ShaderBuilder()
            sb.push()
            val hash = hash(name)
            val resultSym = sb.f()
            functionHash = if (useHash)
                hash(name, returnType, resultSym.name, resultSym.type, sb.code, sb.preamble, sb.tempId)
            else 0U

            if (useHash) {
                generator.emitPreamble("#ifndef f_${name}_${functionHash}")
                generator.emitPreamble("#define f_${name}_${functionHash}")
            }
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}_${functionHash}() { 
${sb.code.prependIndent("    ").trimEnd()}                    
    return ${resultSym.name};
}"""
            )
            if (useHash) {
                generator.emitPreamble("#endif")
            }
            sb.pop()
        }

        operator fun getValue(any: Any?, property: KProperty<*>): () -> Function0Symbol<R> {
            return {
                Function0Symbol(
                    function = "${property.name}_${functionHash}()",
                    type = returnType
                )
            }
        }
    }

    class Function0PropertyProvider<R>(
        private val useHash: Boolean,
        private val generator: Generator,
        private val returnType: String,
        private val f: ShaderBuilder.() -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function0Property<R> =
            Function0Property(useHash, property.name, generator, returnType, f)
    }


    class FunctionPropertyProvider<T, R>(
        private val useHash: Boolean,
        private val generator: Generator,
        private val parameter0Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): FunctionProperty<T, R> =
            FunctionProperty(useHash, property.name, generator, parameter0Type, returnType, f)
    }

    class Function2Property<T0, T1, R>(
        useHash: Boolean,
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>) -> Symbol<R>,
    ) {
        private var functionHash = 0U

        init {
            val sb = ShaderBuilder()
            sb.push()
            val hash = hash(name, parameter0Type, parameter1Type)
            val p0 = "$,,${hash}_0"
            val p1 = "$,,${hash}_1"
            val resultSym = sb.f(symbol(p0, parameter0Type), symbol(p1, parameter1Type))
            generator.emitPreamble(sb.preamble)
            functionHash = if (useHash) hash(
                name,
                returnType,
                parameter0Type,
                parameter1Type,
                resultSym.name,
                resultSym.type,
                sb.code,
                sb.preamble,
                sb.tempId
            ) else 0U

            if (useHash) {
                generator.emitPreamble("#ifndef f_${name}_${functionHash}")
                generator.emitPreamble("#define f_${name}_${functionHash}")
            }
            generator.emitPreamble(
                """$returnType ${name}_${functionHash}($parameter0Type x__, $parameter1Type y__) { 
${sb.code.replace(p0, "x__").replace(p1, "y__").prependIndent("    ").trimEnd()}                    
    return ${resultSym.name.replace(p0, "x__").replace(p1, "y__")};
}"""
            )
            if (useHash) {
                generator.emitPreamble("#endif")
            }
            sb.pop()
        }

        operator fun getValue(
            any: Any?,
            property: KProperty<*>
        ): (Symbol<T0>, Symbol<T1>) -> Function2Symbol<T0, T1, R> {
            return { x, y ->
                Function2Symbol(
                    p0 = x,
                    p1 = y,
                    function = "${property.name}_${functionHash}($0, $1)",
                    type = returnType
                )
            }
        }
    }

    class Function2PropertyProvider<T0, T1, R>(
        private val useHash: Boolean,
        private val generator: Generator,
        private val parameter0Type: String,
        private val parameter1Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T0>, Symbol<T1>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function2Property<T0, T1, R> =
            Function2Property(useHash, property.name, generator, parameter0Type, parameter1Type, returnType, f)
    }

    // Function3
    class Function3PropertyProvider<T0, T1, T2, R>(
        private val useHash: Boolean,
        private val generator: Generator,
        private val parameter0Type: String,
        private val parameter1Type: String,
        private val parameter2Type: String,
        private val returnType: String,
        private val f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Symbol<R>
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): Function3Property<T0, T1, T2, R> =
            Function3Property(useHash, property.name, generator, parameter0Type, parameter1Type, parameter2Type, returnType, f)
    }

    class Function3Property<T0, T1, T2, R>(
        useHash: Boolean,
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        parameter2Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>) -> Symbol<R>,
    ) {
        private var functionHash = 0U
        init {
            val sb = ShaderBuilder()
            sb.push()
            val hash = hash(name, parameter0Type, parameter1Type)
            val p0 = "$,,${hash}_0"
            val p1 = "$,,${hash}_1"
            val p2 = "$,,${hash}_2"
            val resultSym =
                sb.f(symbol(p0, parameter0Type), symbol(p1, parameter1Type), symbol(p2, parameter2Type))

            functionHash = if (useHash) hash(
                name,
                returnType,
                parameter0Type,
                parameter1Type,
                parameter2Type,
                resultSym.name,
                resultSym.type,
                sb.code,
                sb.preamble,
                sb.tempId
            ) else 0U

            if (useHash) {
                generator.emitPreamble("#ifndef f_${name}_${functionHash}")
                generator.emitPreamble("#define f_${name}_${functionHash}")
            }
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}_${functionHash}($parameter0Type x__, $parameter1Type y__, $parameter2Type z__) { 
${sb.code.replace(p0, "x__").replace(p1, "y__").replace(p2, "z__").prependIndent("    ").trimEnd()}                    
    return ${resultSym.name.replace(p0, "x__").replace(p1, "y__").replace(p2, "z__")};
}"""
            )
            if (useHash) {
                generator.emitPreamble("#endif")
            }
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
                    function = "${property.name}_${functionHash}($0, $1, $2)",
                    type = returnType
                )
            }
        }
    }

    class Function4PropertyProvider<T0, T1, T2, T3, R>(
        private val useHash: Boolean,
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
                useHash,
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
        useHash: Boolean,
        name: String,
        private val generator: Generator,
        parameter0Type: String,
        parameter1Type: String,
        parameter2Type: String,
        parameter3Type: String,
        private val returnType: String,
        f: ShaderBuilder.(Symbol<T0>, Symbol<T1>, Symbol<T2>, Symbol<T3>) -> Symbol<R>,
    ) {
        private var functionHash = 0U
        init {
            val sb = ShaderBuilder()
            sb.push()
            val hash = hash(name, parameter0Type, parameter1Type)
            val p0 = "$,,${hash}_0"
            val p1 = "$,,${hash}_1"
            val p2 = "$,,${hash}_2"
            val p3 = "$,,${hash}_3"
            val resultSym = sb.f(symbol(p0, "dc"), symbol(p1, "dc"), symbol(p2, "dc"), symbol(p3, "dc"))

            functionHash = hash(
                name,
                returnType,
                parameter0Type,
                parameter1Type,
                parameter2Type,
                parameter3Type,
                resultSym.name,
                resultSym.type,
                sb.code,
                sb.preamble,
                sb.tempId
            )

            generator.emitPreamble("#ifndef f_${name}_${functionHash}")
            generator.emitPreamble("#define f_${name}_${functionHash}")
            generator.emitPreamble(sb.preamble)
            generator.emitPreamble(
                """$returnType ${name}_${functionHash}($parameter0Type x__, $parameter1Type y__, $parameter2Type z__, $parameter3Type w__) { 
${
                    sb.code.replace(p0, "x__").replace(p1, "y__").replace(p2, "z__").replace(p3, "w__")
                        .prependIndent("    ").trimEnd()
                }                    
    return ${resultSym.name.replace(p0, "x__").replace(p1, "y__").replace(p2, "z__").replace(p3, "z__")};
}"""
            )
            generator.emitPreamble("#endif")
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
                    function = "${property.name}_${functionHash}($0, $1, $2, $3)",
                    type = returnType
                )
            }
        }
    }
}