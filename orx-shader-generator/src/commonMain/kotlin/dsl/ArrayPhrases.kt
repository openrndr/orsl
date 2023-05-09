package org.openrndr.extra.shadergenerator.phrases.dsl

import org.openrndr.extra.shadergenerator.annotations.DynamicShaderPhrase1
import org.openrndr.extra.shadergenerator.annotations.IndexShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderBook
import org.openrndr.extra.shadergenerator.annotations.ShaderPhrase

@IndexShaderBook
class ArrayPhrases : ShaderBook {
    @ShaderPhrase
    val EMPTY_ARRAY = "#define EMPTY_ARRAY 1"

        @DynamicShaderPhrase1
    fun floatArrayMax(length: String) = """|float floatArrayMax_$length(float array[$length]) {
        |    float maxValue = array[0];
        |    for (int i = 1; i < $length; ++i) {
        |        maxValue = max(array[i], maxValue);
        |    }
        |    return maxValue;
        |}
    """.trimMargin()

    @DynamicShaderPhrase1
    fun floatArrayMin(length: String) = """|float floatArrayMin_$length(float array[$length]) {
        |    float minValue = array[0];
        |    for (int i = 1; i < $length; ++i) {
        |        minValue = min(array[i], minValue);
        |    }
        |    return minValue;
        |}
    """.trimMargin()

    @DynamicShaderPhrase1
    fun floatArraySum(length: String) = """|float floatArraySum_$length(float array[$length]) {
        |    float sum = 0.0;
        |    for (int i = 0; i < $length; ++i) {
        |        sum += array[i];
        |    }
        |    return sum;
        |}
    """.trimMargin()

    @ShaderPhrase
    val intArrayMax = """|float floatArrayMax(float array[], int length) {
        |    float maxValue = array[0];
        |    for (int i = 1; i < length; ++i) {
        |        maxValue = max(array[i], maxValue);
        |    }
        |    return maxValue;
        |}
    """.trimMargin()

    @ShaderPhrase
    val intArrayMin = """|float floatArrayMin(float array[], int length) {
        |    float minValue = array[0];
        |    for (int i = 1; i < length; ++i) {
        |        minValue = min(array[i], minValue);
        |    }
        |    return minValue;
        |}
    """.trimMargin()

    @ShaderPhrase
    val intArraySum = """|int intArraySum(int array[], int length) {
        |    float su = 0.0;
        |    for (int i = 0; i < length; ++i) {
        |        sum += array[i];
        |    }
        |    return sum;
        |}
    """.trimMargin()
}