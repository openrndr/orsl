package org.openrndr.extra.shadergenerator.phrases.dsl

import org.openrndr.extra.shadergenerator.phrases.PhraseResolver
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.ArrayFunctions
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.Functions
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.Sampler2DFunctions
import org.openrndr.extra.shadergenerator.phrases.phrases.HashPhrasesFunctions
import org.openrndr.extra.shadergenerator.phrases.phrases.SimplexPhrasesFunctions
import org.openrndr.math.*
import kotlin.reflect.KProperty

open class ShaderBuilder(val resolver: PhraseResolver) : Generator, Functions, ArrayFunctions, Sampler2DFunctions {
    var code = ""
    var preamble = ""
    override fun emitSymbol(symbol: String, f: () -> String) {
        emitPreamble("#pragma import $symbol")
        resolver.functions[symbol] = f
    }

    override fun emitLocalSymbol(symbol: String, f: () -> String) {
        emit("#pragma import $symbol")
        resolver.functions[symbol] = f
    }


    override fun emit(code: String) {
        this.code += code + "\n"
    }

    infix fun Int.until(to: Int): Range {
        val range = Range(startV = this, endV = to)
        return range
    }

    infix fun Symbol<Int>.until(to: Int): Range {
        val range = Range(startP = this, endV = to)
        return range
    }

    infix fun Symbol<Int>.until(to: Symbol<Int>): Range {
        val range = Range(startP = this, endP = to)
        return range
    }


    inline operator fun <reified T> ArraySymbol<T>.provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ArrayValueProperty<T> {
        if (name.isNotEmpty()) {
            emit("${staticType<T>()} ${property.name}[$length] = ${name};")
        } else {
            emit("${staticType<T>()} ${property.name}[$length];")
        }
        return ArrayValueProperty(this@ShaderBuilder, this.length, this.type)
    }

    inline operator fun <reified T> Symbol<T>.provideDelegate(thisRef: Any?, property: KProperty<*>): ValueProperty<T> {
        val hash = (property.name.hashCode() * 31 + staticType<T>().hashCode()).toUInt()
        emitLocalSymbol("DELEGATE_$hash") { "${staticType<T>()} ${property.name} = ${name};" }
        return ValueProperty()
    }


    override fun emitPreamble(code: String) {
        this.preamble += code + "\n"
    }

    inline fun <reified T> variable(): VariableProperty<T> {
        val glslType = staticType<T>()
        return VariableProperty(this@ShaderBuilder, glslType)
    }

    inline fun <reified T> arrayVariable(length: Int): ArrayVariableProperty<T> {
        val glslType = staticType<T>()
        return ArrayVariableProperty(this@ShaderBuilder, length, glslType)
    }

    inline fun <reified T> array(length: Int): ArraySymbol<T> {
        val glslType = staticType<T>()
        return object : ArraySymbol<T> {
            override val name = ""
            override val length = length
            override val type = glslType
        }
    }

    inline fun <reified T> constant(): ConstantProperty<T> {
        return ConstantProperty()
    }


    fun <T> output(): OutputProperty<T> {
        return OutputProperty(this@ShaderBuilder)
    }

    class FunctionProperty<T, R>(
        val generator: Generator,
        val parameter0Type: String,
        val returnType: String,
        f: (Symbol<T>) -> Symbol<R>
    ) {

        var emittedFunction = false
        val id = object : Symbol<T> {
            override val name: String
                get() = "$0"
        }
        val resultSym = f(id)
        operator fun getValue(any: Any?, property: KProperty<*>): (Symbol<T>) -> FunctionSymbol1<T, R> {
            if (!emittedFunction) {
                generator.emitSymbol("${property.name}") {
                    """$returnType ${property.name}($parameter0Type x) { 
    return ${resultSym.name.replace("$0", "x")};
}"""
                }
                emittedFunction = true
            }

            return { x ->
                FunctionSymbol1(p0 = x, function = "${property.name}($0)")
            }

        }
    }

    inline fun <reified T, reified R> function(noinline f: (Symbol<T>) -> Symbol<R>): FunctionProperty<T, R> {
        val ttype = staticType<T>()
        val rtype = staticType<R>()
        return FunctionProperty(this@ShaderBuilder, parameter0Type = ttype, returnType = rtype, f)
    }


    inline fun <reified R> BoxRange2.weightedAverageBy(
        noinline itemFunction: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>,
        noinline weightFunction: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, Double>
    ): Symbol<R> {

        val id = object : Symbol<IntVector2> {
            override val name: String
                get() = "$0"
        }
        val itemFunctionId = itemFunction(id)
        val weightFunctionId = weightFunction(id)
        val returnType = staticType<R>()

        var thash = itemFunctionId.name.hashCode()
        thash = thash * 31 + weightFunctionId.name.hashCode()
        val hash = thash.toUInt()

        emitSymbol("weightedAverageBy_${hash}") {
            """$returnType weightedAverageBy_${hash}(int startX, int endX, int startY, int endY) {
    $returnType sum = ${zero<R>()};
    float weight = 0.0;
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
           sum += ${itemFunctionId.function.replace("$0", "ivec2(i, j)")};
           weight += ${weightFunctionId.function.replace("$0", "ivec2(i, j)")};
       }
    }
    return sum / weight;
}"""
        }
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no startX")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no startY")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no endX")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no endY")
        return object : Symbol<R> {
            override val name = "weightedAverageBy_${hash}($startX, $endX, $startY, $endY)"
        }
    }

    inline fun <reified R> BoxRange2.sumBy(noinline function: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>): Symbol<R> {
        val hash = (function.hashCode() * 31 + this.hashCode()).toUInt()
        val id = object : Symbol<IntVector2> {
            override val name: String
                get() = "$0"
        }
        val functionId = function(id)
        val returnType = staticType<R>()

        emitSymbol("sumBy_${hash}") {
            """$returnType sumBy_${hash}(int startX, int endX, int startY, int endY) {
    $returnType sum = ${zero<R>()};
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
           sum += ${functionId.function.replace("$0", "ivec2(i, j)")};
       }
    }
    return sum;
}"""
        }
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no start")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no start")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no start")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no start")
        return object : Symbol<R> {
            override val name = "sumBy_${hash}($startX, $endX, $startY, $endY)"
        }
    }

    inline fun <reified R> Range.sumBy(noinline function: (x: Symbol<Int>) -> FunctionSymbol1<Int, R>): Symbol<R> {
        val id = object : Symbol<Int> {
            override val name: String
                get() = "$0"
        }
        val functionId = function(id)
        val returnType = staticType<R>()

        var thash = functionId.name.hashCode()
        thash = thash * 31 + returnType.hashCode()
        val hash = thash.toUInt()

        emitSymbol("sumBy_${hash}") {
            """$returnType sumBy_${hash}(int start, int end) {
    $returnType sum = ${zero<R>()}; 
    for (int i = start; i < end; ++i) {
        sum += ${functionId.function.replace("$0", "i")};
    }
    return sum;
}"""
        }
        val start = startP?.name ?: startV?.toString() ?: error("no start")
        val end = endP?.name ?: endV?.toString() ?: error("no end")
        return object : Symbol<R> {
            override val name = "sumBy_${hash}($start, $end)"
        }
    }

    inline fun <reified T, reified R> ArraySymbol<T>.map(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, R>): ArraySymbol<R> {
        val id = object : Symbol<T> {
            override val name: String
                get() = "$0"
        }
        val functionId = function(id)
        val returnType = staticType<R>()
        val inputType = staticType<T>()

        var thash = functionId.name.hashCode()
        thash = thash * 31 + returnType.hashCode()
        thash = thash * 31 + inputType.hashCode()
        val hash = thash.toUInt()

        emitSymbol("map_${hash}") {
            """$returnType[${length}] map_${hash}($inputType x[${length}]) {
    $returnType[$length] y;
    for (int i = 0; i < $length; ++i) {
        y[i] = ${functionId.function.replace("$0", "x[i]")};
    }
    return y;
}"""
        }
        return object : ArraySymbol<R> {
            override val name = "map_${hash}(${this@map.name})"
            override val length = this@map.length
            override val type = "float"
        }
    }
}

open class ShadeStyleBuilder(resolver: PhraseResolver) : ShaderBuilder(resolver) {
    class Parameter<T> {
        operator fun getValue(any: Any?, property: KProperty<*>): Symbol<T> {
            return object : Symbol<T> {
                override val name: String
                    get() = property.name
            }
        }
    }

    fun <T> parameter(): Parameter<T> {
        return Parameter()
    }
}

class FragmentTransformBuilder(resolver: PhraseResolver) : ShadeStyleBuilder(resolver), HashPhrasesFunctions,
    SimplexPhrasesFunctions {
    var color by output<Vector4>()
    var x_fill by output<Vector4>()
    var x_stroke by output<Vector4>()

    val v_worldNormal by variable<Vector3>()
    val v_viewNormal by variable<Vector3>()
    val v_worldPosition by variable<Vector3>()
    val v_viewPosition by variable<Vector3>()
    val v_clipPosition by variable<Vector4>()
    val v_modelNormalMatrix by variable<Matrix44>()

    val c_boundsPosition by variable<Vector3>()
    val c_boundsSize by variable<Vector3>()
    val c_screenPosition by variable<Vector2>()
    val c_contourPosition by variable<Double>()
    val c_instance by variable<Int>()
    val c_element by variable<Int>()
}