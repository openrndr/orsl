package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.color.ColorRGBa
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.glsl
import org.openrndr.extra.shadergenerator.dsl.symbol
import org.openrndr.math.Vector3
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface IntFunctions {


    @JvmName("absSi")
    fun abs(x: Symbol<Int>): Symbol<Int> = functionSymbol(x, "abs($0)")

    @JvmName("minSiSi")
    fun min(a: Symbol<Int>, b: Symbol<Int>): Symbol<Int> = functionSymbol(a, b, "min($0, $1)")

    @JvmName("maxSisi")
    fun max(a: Symbol<Int>, b: Symbol<Int>): Symbol<Int> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("unaryMinusSi")
    operator fun Symbol<Int>.unaryMinus(): Symbol<Int> =
        functionSymbol(this, "(-$0)")

    @JvmName("plusSiSi")
    operator fun Symbol<Int>.plus(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiSd")
    operator fun Symbol<Int>.plus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiVi")
    operator fun Symbol<Int>.plus(right: Int): Symbol<Int> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSiVd")
    operator fun Symbol<Int>.plus(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSiSi")
    operator fun Symbol<Int>.minus(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("timesSiSi")
    operator fun Symbol<Int>.times(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiVi")
    operator fun Symbol<Int>.times(right: Int): Symbol<Int> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSiVd")
    operator fun Symbol<Int>.times(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("divSiSi")
    operator fun Symbol<Int>.div(right: Symbol<Int>): Symbol<Int> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSiSd")
    operator fun Symbol<Int>.div(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSiVd")
    operator fun Symbol<Int>.div(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")



    @JvmName("eqSiSi")
    infix fun Symbol<Int>.eq(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("eqSiVi")
    infix fun Symbol<Int>.eq(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("neqSiSi")
    infix fun Symbol<Int>.neq(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("neqSiVi")
    infix fun Symbol<Int>.neq(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("gteSiSi")
    infix fun Symbol<Int>.gte(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gteSiVi")
    infix fun Symbol<Int>.gte(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gtSiSi")
    infix fun Symbol<Int>.gt(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("gtSiVi")
    infix fun Symbol<Int>.gt(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("ltSiSi")
    infix fun Symbol<Int>.lt(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("ltSiVi")
    infix fun Symbol<Int>.lt(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("lteSiSi")
    infix fun Symbol<Int>.lte(right: Symbol<Int>) : Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    @JvmName("lteSiVi")
    infix fun Symbol<Int>.lte(right: Int) : Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    /**
     * find the index of the most significant bit set to 1 in an integer
     * @since GLSL 4.00
     */
    @JvmName("findMSBSi")
    fun Symbol<Int>.findMSB(): Symbol<Int> = functionSymbol(this, "findMSB($0)")


    /**
     * find the index of the least significant bit set to 1 in an integer
     * @since GLSL 4.00
     */
    @JvmName("findLSBSi")
    fun Symbol<Int>.findLSB(): Symbol<Int> = functionSymbol(this, "findLSB($0)")

    /**
     * produce a floating point using an encoding supplied as an integer
     * @since GLSL 3.30
     */
    @JvmName("bitsToFloatSi")
    fun Symbol<Int>.bitsToFloat(): Symbol<Double> = functionSymbol(this, "intBitsToFloat($0)")

    /**
     * counts the number of 1-bits in an integer
     * @since GLSL 4.00
     */
    @JvmName("bitCountSi")
    fun Symbol<Int>.bitCount(): Symbol<Int> = functionSymbol(this, "bitCount($0)")

    /**
     * extract a range of bits from an integer
     * @param offset Specifies the index of the first bit to extract.
     * @param bits Specifies the number of bits to extract.
     * @since GLSL 4.00
     */
    @JvmName("bitfieldExtractSiSiSi")
    fun Symbol<Int>.bitfieldExtract(offset: Symbol<Int>, bits: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, offset, bits, "bitfieldExtract($0, $1, $2)")

    /**
     *  insert a range of bits into an integer
     *  @param insert Specifies the value of the bits to insert.
     *  @param offset Specifies the index of the first bit to insert.
     *  @param bits Specifies the number of bits to insert.
     *  @since GLSL 4.00
     */
    @JvmName("bitfieldInsertSiSiSiSi")
    fun Symbol<Int>.bitfieldInsert(insert: Symbol<Int>, offset: Symbol<Int>, bits: Symbol<Int>): Symbol<Int> =
        functionSymbol(this, insert, offset, bits, "bitfieldInsert($0)")

    /**
     * reverse the order of bits in an integer
     * @since GLSL 4.00
     */
    @JvmName("bitfieldReverseSi")
    fun Symbol<Int>.bitfieldReverse(): Symbol<Int> =
        functionSymbol(this, "bitfieldReverse($0)")

    val Symbol<Int>.uint: Symbol<UInt>
        @JvmName("intSi")
        get() = functionSymbol(this, "uint($0)")

    val Symbol<Int>.double: Symbol<Double>
        @JvmName("doubleSi")
        get() = functionSymbol(this, "float($0)")
}

val Int.symbol: Symbol<Int>
    get() = symbol(glsl(this)!!)
