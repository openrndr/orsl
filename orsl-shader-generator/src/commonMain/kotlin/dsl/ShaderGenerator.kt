@file:Suppress("PackageDirectoryMismatch")

package org.openrndr.orsl.shadergenerator.dsl

import org.openrndr.draw.Struct
import org.openrndr.draw.typeDef
import org.openrndr.orsl.shadergenerator.dsl.functions.*
import org.openrndr.orsl.shadergenerator.dsl.functions.BooleanFunctions
import org.openrndr.orsl.shadergenerator.dsl.functions.Matrix33Functions
import org.openrndr.orsl.shadergenerator.dsl.functions.SamplerFunctions
import org.openrndr.orsl.shadergenerator.dsl.functions.ColorRGBaFunctions
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.AtomicCounterBufferFunctions
import org.openrndr.orsl.shadergenerator.phrases.dsl.functions.BarrierFunctions
import org.openrndr.math.*
import kotlin.reflect.KProperty

open class ShaderBuilder(declaredSymbols: Set<String>) : Generator, Functions, BooleanFunctions, DoubleFunctions, ArrayFunctions, SamplerFunctions,
    Vector3Functions,
    IntFunctions, ColorRGBaFunctions,  UIntFunctions,
    Vector2Functions, Vector4Functions, Matrix33Functions, Matrix44Functions,
    IntVector2Functions, IntVector3unctions, UIntVector2Functions,
    UIntVector3Functions, AtomicCounterBufferFunctions, IntRImage2DFunctions, RImage3DFunctions,
    IntRImage3DFunctions,
    BarrierFunctions {
    override var code = ""
    override var preamble = ""
    override val declaredSymbols= declaredSymbols.map { it }.toMutableSet()


    infix fun Int.until(to: Int): Range {
        return Range(startV = this, endV = to)
    }

    infix fun Symbol<Int>.until(to: Int): Range {
        return Range(startP = this, endV = to)
    }


    infix fun Symbol<Int>.until(to: Symbol<Int>): Range = Range(startP = this, endP = to)

    operator fun Symbol<Int>.rangeTo(to: Symbol<Int>): Range = Range(startP = this, endP = to + symbol<Int>("1"))
    operator fun Int.rangeTo(to: Symbol<Int>): Range = Range(startV = this, endP = to + symbol<Int>("1"))
    operator fun Symbol<Int>.rangeTo(to: Int): Range = Range(startP = this, endV = to - 1)

    operator fun Int.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Int> {
        emit("int ${property.name} = $this;")
        return ConstantProperty("int")
    }

    operator fun Boolean.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Boolean> {
        emit("${staticType<Boolean>()} ${property.name} = $this;")
        return ConstantProperty(staticType<Boolean>())
    }

    operator fun Vector4.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Vector4> {
        emit("${staticType<Vector4>()} ${property.name} = ${glsl(this)};")
        return ConstantProperty(staticType<Vector4>())
    }

    operator fun Vector3.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Vector3> {
        emit("${staticType<Vector3>()} ${property.name} = ${glsl(this)};")
        return ConstantProperty(staticType<Vector3>())
    }


    inline operator fun <reified T : EuclideanVector<T>> T.provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ConstantProperty<T> {
        emit("${staticType<T>()} ${property.name} = ${glsl(this)};")
        return ConstantProperty(staticType<T>())
    }

    operator fun Matrix44.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Matrix44> {
        emit("mat4 ${property.name} = ${glsl(this)};")
        return ConstantProperty("mat4")
    }

    operator fun Matrix33.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Matrix33> {
        emit("mat3 ${property.name} = $this;")
        return ConstantProperty("mat3")
    }


    operator fun Double.provideDelegate(thisRef: Any?, property: KProperty<*>): ConstantProperty<Double> {
        emit("float ${property.name} = $this;")
        return ConstantProperty("float")
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
        emit("$type ${property.name} = ${name};")
        return ValueProperty(type)
    }

//    inline operator fun <reified T:Struct<T>> Symbol<T>

    private val emitted = mutableSetOf<String>()

    override fun emitPreamble(code: String) {
        if (code.isNotBlank()) {
            if (code !in emitted) {
                this.preamble += code.trimEnd() + "\n"
                if (code.split("\n").size > 1) {
                    emitted.add(code)
                }
            }
        }
    }


    inline fun <reified T> global(): GlobalProperty<T> {
        val glslType = staticType<T>()
        return GlobalProperty(this@ShaderBuilder, glslType)
    }


    inline fun <reified T> variable(): VariableProperty<T> {
        val glslType = staticType<T>()
        return VariableProperty(this@ShaderBuilder, glslType, null, null)
    }

    inline fun <reified T> variable(initialValue: T): VariableProperty<T> {
        val glslType = staticType<T>()
        return VariableProperty(this@ShaderBuilder, glslType, initialValue, null)
    }

    inline fun <reified T> variable(initialValue: Symbol<T>): VariableProperty<T> {
        val glslType = staticType<T>()
        return VariableProperty(this@ShaderBuilder, glslType, null, initialValue)
    }

    inline fun <reified T> arrayVariable(length: Int): ArrayVariableProperty<T> {
        val glslType = staticType<T>()
        return ArrayVariableProperty(this@ShaderBuilder, length, glslType)
    }


    inline fun <reified T> array(length: Int): ArraySymbol<T> = arraySymbol("", length)


    inline fun <reified T> constant(): ConstantProperty<T> {
        return ConstantProperty(staticType<T>())
    }

    inline fun <reified T> output(): OutputProperty<T> {
        return OutputProperty(this@ShaderBuilder, staticType<T>())
    }


    inline fun <reified T> fragmentOutput(): FragmentOutputProperty<T> {
        return FragmentOutputProperty(this@ShaderBuilder, staticType<T>())
    }

    @PublishedApi
    internal var tempId = 1
    inline fun <reified T> Symbol<T>.elseIf(
        precondition: Symbol<Boolean>,
        noinline f: ShaderBuilder.() -> Symbol<T>
    ): Symbol<T> {
        val sb = ShaderBuilder(declaredSymbols)
        sb.push()
        val result = sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """${staticType<T>()} temp_$tempId; 
if (${precondition.name}) {
${sb.code.prependIndent("    ").trimEnd()}
    temp_$tempId = ${result.name};
} else { 
    temp_$tempId = ${this@elseIf.name};
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        val s = symbol<T>("temp_$tempId")
        tempId++
        return s
    }

    @Suppress("FunctionName")
    fun Symbol<Int>.for_(range: Range, f: ShaderBuilder.() -> Unit) {
        val sb = ShaderBuilder(declaredSymbols)
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """for (${this.name} = ${range.startV}; ${this.name} < ${range.endV}; ++${this.name}) {
${sb.code.prependIndent("    ").trimEnd()}            
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
    }

    @Suppress("FunctionName")
    fun break_() {
        emit("break;")
    }

    @Suppress("FunctionName")
    fun continue_() {
        emit("continue;")
    }

    inline fun <reified T> run(noinline f: ShaderBuilder.() -> Symbol<T>): Symbol<T> {
        val sb = ShaderBuilder(declaredSymbols)
        sb.tempId = tempId * 31
        sb.push()
        val result = sb.f()
        sb.pop()
        val hash = hash(this.code, this.preamble)
        emitPreamble(sb.preamble)
        emit(
            """${staticType<T>()} temp_${hash}_$tempId; 
{
${sb.code.prependIndent("    ").trimEnd()}
    temp_${hash}_$tempId = ${result.name};
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        val s = ifSymbol<T>("temp_${hash}_$tempId")
        tempId++
        return s
    }


    @Suppress("FunctionName")
    inline fun <reified T> if_(precondition: Symbol<Boolean>, noinline f: ShaderBuilder.() -> Symbol<T>): IfSymbol<T> {
        val sb = ShaderBuilder(declaredSymbols)
        sb.push()
        sb.tempId = tempId * 31
        val result = sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """${staticType<T>()} temp_$tempId; 
if (${precondition.name}) {
${sb.code.prependIndent("    ").trimEnd()}
    temp_$tempId = ${result.name};
}"""
        )
        val s = ifSymbol<T>("temp_$tempId")
        declaredSymbols.addAll(sb.declaredSymbols)
        tempId++
        return s
    }

    fun doIf(precondition: Symbol<Boolean>, f: ShaderBuilder.() -> Unit): DoIfSymbol {
        val sb = ShaderBuilder(declaredSymbols)
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """if (${precondition.name}) {
${sb.code.prependIndent("    ").trimEnd()}
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        return object: DoIfSymbol {
            override val name: String
                get() = ""
            override val type: String
                get() = "void"

        }
    }

    @Suppress("FunctionName")
    inline infix fun <reified T> IfSymbol<T>.else_(noinline f: ShaderBuilder.() -> Symbol<T>): Symbol<T> {
        val sb = ShaderBuilder(declaredSymbols)
        sb.tempId = tempId * 31
        sb.push()
        val result = sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """else { 
${sb.code.prependIndent("    ").trimEnd()}
    temp_${tempId - 1} = ${result.name};             
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        return symbol<T>("temp_${tempId - 1}")
    }

    /**
     * non-expression else
     */
    @Suppress("FunctionName")
    inline infix fun DoIfSymbol.else_(noinline f: ShaderBuilder.() -> Unit) {
        val sb = ShaderBuilder(declaredSymbols)
        sb.tempId = tempId * 31
        sb.push()
        sb.f()
        sb.pop()
        emitPreamble(sb.preamble)
        emit(
            """else { 
${sb.code.prependIndent("    ").trimEnd()}
               
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
    }

    @Suppress("UnusedReceiverParameter")
    inline fun <reified T> IfSymbol<T>.elseIf(
        precondition: Symbol<Boolean>,
        noinline f: ShaderBuilder.() -> Symbol<T>
    ): IfSymbol<T> {
        val sb = ShaderBuilder(declaredSymbols)
        sb.tempId = tempId * 31
        sb.push()
        val result = sb.f()
        sb.pop()

        emitPreamble(sb.preamble)
        emit(
            """else if (${precondition.name}) {
${sb.code.prependIndent("    ").trimEnd()}
    temp_${tempId - 1} = ${result.name};
}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        return ifSymbol<T>("temp_${tempId - 1}")
    }


    @Suppress("UnusedReceiverParameter")
    inline fun <reified T> DoIfSymbol.elseIf(
        precondition: Symbol<Boolean>,
        noinline f: ShaderBuilder.() -> Unit
    ): DoIfSymbol {
        val sb = ShaderBuilder(declaredSymbols)
        sb.push()
        sb.f()
        sb.pop()

        emitPreamble(sb.preamble)
        emit(
            """else if (${precondition.name}) {
${sb.code.prependIndent("    ").trimEnd()}

}"""
        )
        declaredSymbols.addAll(sb.declaredSymbols)
        return object: DoIfSymbol {
            override val name: String
                get() = ""
            override val type: String
                get() = "void"

        }
    }



    inline fun <reified R> BoxRange2.weightedAverageBy(
        noinline itemFunction: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>,
        noinline weightFunction: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, Double>
    ): Symbol<R> {
        val id = symbol<IntVector2>("$0")
        val itemFunctionId = itemFunction(id)
        val weightFunctionId = weightFunction(id)
        val returnType = staticType<R>()
        val hash = hash(itemFunctionId.name, weightFunctionId.name, returnType)

        emitPreamble(
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
        )
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no startX")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no startY")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no endX")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no endY")
        return symbol("weightedAverageBy_${hash}($startX, $endX, $startY, $endY)")
    }

    inline fun <reified R> BoxRange2.sumBy(noinline function: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>): Symbol<R> {
        val id = symbol<IntVector2>("$0")
        val functionId = function(id)
        val returnType = staticType<R>()
        val hash = hash(functionId.name, this, returnType)
        emit(
            """$returnType sumBy_${hash}(int startX, int endX, int startY, int endY) {
    $returnType sum = ${zero<R>()};
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
           sum += ${functionId.function.replace("$0", "ivec2(i, j)")};
       }
    }
    return sum;
}"""
        )
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no startX")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no startY")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no endX")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no endY")

        return symbol("sumBy_${hash}($startX, $endX, $startY, $endY)")
    }

    inline fun <reified R> BoxRange2.minBy(noinline function: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>): Symbol<R> {
        val id = symbol<IntVector2>("$0")
        val functionId = function(id)
        val returnType = staticType<R>()
        val hash = hash(functionId.name, this, returnType)
        emitPreamble(
            """$returnType minBy_${hash}(int startX, int endX, int startY, int endY) {
    $returnType minV = ${functionId.function.replace("$0", "ivec2(startX, startY)")};
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
           minV = min(minV, ${functionId.function.replace("$0", "ivec2(i, j)")});
       }
    }
    return minV;
}"""
        )
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no startX")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no startY")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no endX")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no endY")

        return symbol("minBy_${hash}($startX, $endX, $startY, $endY)")
    }

    inline fun <reified R> BoxRange2.foldBy(
        init: Symbol<R>,
        accumulate: (acc: Symbol<R>, elem: Symbol<R>) -> Function2Symbol<R, R, R>,
        noinline function: (x: Symbol<IntVector2>) -> FunctionSymbol1<IntVector2, R>
    ): Symbol<R> {
        val id = symbol<IntVector2>("$0")


        val accId = symbol<R>("$0")
        val elemId = symbol<R>("$1")

        val accFunctionId = accumulate(accId, elemId)
        val functionId = function(id)
        val returnType = staticType<R>()
        val hash = hash(functionId.name, this, returnType)
        emitPreamble(
            """$returnType foldBy_${hash}(${staticType<R>()} init, int startX, int endX, int startY, int endY) {
    $returnType acc = init;
    for (int j = startY; j < endY; ++j) {
       for (int i = startX; i < endX; ++i) {
            $returnType elem = ${functionId.function.replace("$0", "ivec2(i, j)")}; 
            acc = ${accFunctionId.function.replace("$0", "acc").replace("$1", "elem")};
       }
    }
    return acc;
}"""
        )
        val startX = xrange.startP?.name ?: xrange.startV?.toString() ?: error("no startX")
        val startY = yrange.startP?.name ?: yrange.startV?.toString() ?: error("no startY")
        val endX = xrange.endP?.name ?: xrange.endV?.toString() ?: error("no endX")
        val endY = yrange.endP?.name ?: yrange.endV?.toString() ?: error("no endY")

        return symbol("foldBy_${hash}(${init.name}, $startX, $endX, $startY, $endY)")
    }


    inline fun <reified R> Range.sumBy(noinline function: (x: Symbol<Int>) -> FunctionSymbol1<Int, R>): Symbol<R> {
        val id = symbol<Int>("$0")
        val functionId = function(id)
        val returnType = staticType<R>()
        val hash = hash(functionId.name, returnType)

        emitPreamble(
            """$returnType sumBy_${hash}(int start, int end) {
    $returnType sum = ${zero<R>()}; 
    for (int i = start; i < end; ++i) {
        sum += ${functionId.function.replace("$0", "i")};
    }
    return sum;
}"""
        )
        val start = startP?.name ?: startV?.toString() ?: error("no start")
        val end = endP?.name ?: endV?.toString() ?: error("no end")
        return symbol("sumBy_${hash}($start, $end)")
    }


    inline fun <reified T, reified R> ArraySymbol<T>.map(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, R>): ArraySymbol<R> {
        val id = symbol<T>("$0")
        val functionId = function(id)
        val returnType = staticType<R>()
        val inputType = staticType<T>()
        val hash = hash(functionId.name, returnType, inputType)

        emitPreamble(
            """$returnType[${length}] map_${hash}($inputType x[${length}]) {
    $returnType[$length] y;
    for (int i = 0; i < $length; ++i) {
        y[i] = ${functionId.function.replace("$0", "x[i]")};
    }
    return y;
}"""
        )
        return object : ArraySymbol<R> {
            override val name = "map_${hash}(${this@map.name})"
            override val length = this@map.length
            override val type = "float"
        }
    }


    inline fun <reified T> ArraySymbol<T>.minBy(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, Double>): Symbol<T> {
        val id = symbol<T>("$0")
        val functionId = function(id)
        val returnType = staticType<T>()
        val inputType = staticType<T>()
        val hash = hash(functionId.name, returnType, inputType)

        emitPreamble(
            """$returnType minBy_${hash}($inputType x__[${length}]) {
    float minValue = ${functionId.function.replace("$0", "x__[0]")};
    int index = 0;
    for (int i = 1; i < $length; ++i) {
        float candidateValue = ${functionId.function.replace("$0", "x__[i]")};
        if (candidateValue < minValue) {
            minValue = candidateValue;
            index = i;
        }
    }
    return x__[index];
}"""
        )
        return symbol(name = "minBy_${hash}(${this@minBy.name})", type = this@minBy.type)
    }

    inline fun <reified T> ArraySymbol<T>.argMinBy(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, Double>): Symbol<Int> {
        val id = symbol<T>("$0")
        val functionId = function(id)
        val returnType = staticType<Int>()
        val inputType = staticType<T>()
        val hash = hash(functionId.name, returnType, inputType)

        emitPreamble(
            """$returnType argMinBy_${hash}($inputType x__[${length}]) {
    float minValue = ${functionId.function.replace("$0", "x__[0]")};
    int index = 0;
    for (int i = 1; i < $length; ++i) {
        float candidateValue = ${functionId.function.replace("$0", "x__[i]")};
        if (candidateValue < minValue) {
            minValue = candidateValue;
            index = i;
        }
    }
    return index;
}"""
        )
        return symbol(name = "argMinBy_${hash}(${this@argMinBy.name})", type = "int")
    }


    inline fun <reified T> ArraySymbol<T>.maxBy(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, Double>): Symbol<T> {
        val id = symbol<T>("$0")
        val functionId = function(id)
        val returnType = staticType<T>()
        val inputType = staticType<T>()
        val hash = hash(functionId.name, returnType, inputType)

        emitPreamble(
            """$returnType maxBy_${hash}($inputType x__[${length}]) {
    float maxValue = ${functionId.function.replace("$0", "x__[0]")};
    int index = 0;
    for (int i = 1; i < $length; ++i) {
        float candidateValue = ${functionId.function.replace("$0", "x__[i]")};
        if (candidateValue > maxValue) {
            maxValue = candidateValue;
            index = i;
        }
    }
    return x__[index];
}"""
        )
        return symbol(name = "maxBy_${hash}(${this@maxBy.name})", type = this@maxBy.type)
    }


    inline fun <reified T> ArraySymbol<T>.argMaxBy(noinline function: (x: Symbol<T>) -> FunctionSymbol1<T, Double>): Symbol<Int> {
        val id = symbol<T>("$0")
        val functionId = function(id)
        val returnType = staticType<Int>()
        val inputType = staticType<T>()
        val hash = hash(functionId.name, returnType, inputType)

        emitPreamble(
            """$returnType argMaxBy_${hash}($inputType x__[${length}]) {
    float maxValue = ${functionId.function.replace("$0", "x__[0]")};
    int index = 0;
    for (int i = 1; i < $length; ++i) {
        float candidateValue = ${functionId.function.replace("$0", "x__[i]")};
        if (candidateValue > maxValue) {
            minValue = candidateValue;
            index = i;
        }
    }
    return index;
}"""
        )
        return symbol(name = "argMaxBy_${hash}(${this@argMaxBy.name})", type = "int")
    }

    inline operator fun <reified T : Struct<T>> T.provideDelegate(any: Any?, property: KProperty<*>): T {
        emitPreamble(this.typeDef())
        emit("${this::class.simpleName} ${property.name};")
        return this
    }
}