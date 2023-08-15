package org.openrndr.extra.shadergenerator.dsl.functions

import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functionSymbol
import org.openrndr.extra.shadergenerator.dsl.glsl
import org.openrndr.extra.shadergenerator.dsl.symbol
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
interface UIntFunctions {

    @JvmName("minSuiSui")
    fun min(a: Symbol<UInt>, b: Symbol<UInt>): Symbol<UInt> = functionSymbol(a, b, "min($0, $1)")

    @JvmName("maxSuiSui")
    fun max(a: Symbol<UInt>, b: Symbol<UInt>): Symbol<UInt> = functionSymbol(a, b, "max($0, $1)")

    @JvmName("plusSuiSui")
    operator fun Symbol<UInt>.plus(right: Symbol<UInt>): Symbol<UInt> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("xorSuiVui")
    infix fun Symbol<UInt>.xor(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 ^ $1)")

    @JvmName("xorSuiSui")
    infix fun Symbol<UInt>.xor(right: Symbol<UInt>): Symbol<UInt> = functionSymbol(this, right, "($0 ^ $1)")

    @JvmName("orSuiVui")
    infix fun Symbol<UInt>.or(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 | $1)")
    @JvmName("orSuiSui")
    infix fun Symbol<UInt>.or(right: Symbol<UInt>): Symbol<UInt> = functionSymbol(this, right, "($0 | $1)")


    @JvmName("andSuiVui")
    infix fun Symbol<UInt>.and(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 & $1)")

    infix fun Symbol<UInt>.shr(right: Int): Symbol<UInt> = functionSymbol(this, right, "($0 >> $1)")

    @JvmName("shrSuiVui")
    infix fun Symbol<UInt>.shr(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 << $1)")


    @JvmName("shlSuiVi")
    infix fun Symbol<UInt>.shl(right: Int): Symbol<Int> = functionSymbol(this, right, "($0 << $1)")

    @JvmName("shlSuiVui")
    infix fun Symbol<UInt>.shl(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 << $1)")

    @JvmName("shlSuiSui")
    infix fun Symbol<UInt>.shl(right: Symbol<UInt>): Symbol<UInt> = functionSymbol(this, right, "($0 << $1)")


    @JvmName("plusSuiSd")
    operator fun Symbol<UInt>.plus(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSuiVui")
    operator fun Symbol<UInt>.plus(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("plusSuiVd")
    operator fun Symbol<UInt>.plus(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 + $1)")

    @JvmName("minusSuiSui")
    operator fun Symbol<UInt>.minus(right: Symbol<UInt>): Symbol<Int> = functionSymbol(this, right, "($0 - $1)")

    @JvmName("timesSuiSui")
    operator fun Symbol<UInt>.times(right: Symbol<UInt>): Symbol<UInt> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSuiVui")
    operator fun Symbol<UInt>.times(right: UInt): Symbol<UInt> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("timesSuiVd")
    operator fun Symbol<UInt>.times(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 * $1)")

    @JvmName("divSuiSui")
    operator fun Symbol<UInt>.div(right: Symbol<UInt>): Symbol<Int> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSuiSd")
    operator fun Symbol<UInt>.div(right: Symbol<Double>): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("divSuiVd")
    operator fun Symbol<UInt>.div(right: Double): Symbol<Double> = functionSymbol(this, right, "($0 / $1)")

    @JvmName("eqSuiSui")
    infix fun Symbol<UInt>.eq(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("eqSuiVui")
    infix fun Symbol<UInt>.eq(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 == $1)")

    @JvmName("neqSuiSui")
    infix fun Symbol<UInt>.neq(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("neqSuiVui")
    infix fun Symbol<UInt>.neq(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 != $1)")

    @JvmName("gteSuiSui")
    infix fun Symbol<UInt>.gte(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gteSuiVui")
    infix fun Symbol<UInt>.gte(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 >= $1)")

    @JvmName("gtSuiSui")
    infix fun Symbol<UInt>.gt(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("gtSuiVui")
    infix fun Symbol<UInt>.gt(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 > $1)")

    @JvmName("ltSuiSui")
    infix fun Symbol<UInt>.lt(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("ltSuiVui")
    infix fun Symbol<UInt>.lt(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 < $1)")

    @JvmName("lteSuiSui")
    infix fun Symbol<UInt>.lte(right: Symbol<UInt>): Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    @JvmName("lteSuiVui")
    infix fun Symbol<UInt>.lte(right: UInt): Symbol<Boolean> = functionSymbol(this, right, "($0 <= $1)")

    /**
     * find the index of the most significant bit set to 1 in an integer
     * @since GLSL 4.00
     */
    @JvmName("findMSBSui")
    fun Symbol<UInt>.findMSB(): Symbol<Int> = functionSymbol(this, "findMSB($0)")


    /**
     * find the index of the least significant bit set to 1 in an integer
     * @since GLSL 4.00
     */
    @JvmName("findLSBSui")
    fun Symbol<UInt>.findLSB(): Symbol<Int> = functionSymbol(this, "findLSB($0)")

    /**
     * produce a floating point using an encoding supplied as an integer
     * @since GLSL 3.30
     */
    @JvmName("bitsToFloatSui")
    fun Symbol<UInt>.bitsToFloat(): Symbol<Double> = functionSymbol(this, "uintBitsToFloat($0)")

    /**
     * counts the number of 1-bits in an integer
     * @since GLSL 4.00
     */
    @JvmName("bitCountSui")
    fun Symbol<UInt>.bitCount(): Symbol<Int> = functionSymbol(this, "bitCount($0)")

    /**
     * extract a range of bits from an integer
     * @param offset Specifies the index of the first bit to extract.
     * @param bits Specifies the number of bits to extract.
     * @since GLSL 4.00
     */
    @JvmName("bitfieldExtractSuiSiSi")
    fun Symbol<UInt>.bitfieldExtract(offset: Symbol<Int>, bits: Symbol<Int>): Symbol<UInt> =
        functionSymbol(this, offset, bits, "bitfieldExtract($0, $1, $2)")

    /**
     *  insert a range of bits into an integer
     *  @param insert Specifies the value of the bits to insert.
     *  @param offset Specifies the index of the first bit to insert.
     *  @param bits Specifies the number of bits to insert.
     *  @since GLSL 4.00
     */
    @JvmName("bitfieldInsertSuiSuiSiSi")
    fun Symbol<UInt>.bitfieldInsert(insert: Symbol<UInt>, offset: Symbol<Int>, bits: Symbol<Int>): Symbol<UInt> =
        functionSymbol(this, insert, offset, bits, "bitfieldInsert($0)")

    /**
     * reverse the order of bits in an integer
     * @since GLSL 4.00
     */
    @JvmName("bitfieldReverseSui")
    fun Symbol<UInt>.bitfieldReverse(): Symbol<UInt> =
        functionSymbol(this, "bitfieldReverse($0)")

    val Symbol<UInt>.int: Symbol<Int>
        @JvmName("intSui")
        get() = functionSymbol(this, "int($0)")

    val Symbol<UInt>.double: Symbol<Double>
        @JvmName("doubleSui")
        get() = functionSymbol(this, "float($0)")

}

val UInt.symbol: Symbol<UInt>
    get() = symbol(glsl(this)!!)