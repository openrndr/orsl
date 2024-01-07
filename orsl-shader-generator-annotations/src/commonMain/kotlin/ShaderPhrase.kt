package org.openrndr.orsl.shadergenerator.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ShaderPhrase()

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DynamicShaderPhrase1()

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class IndexShaderBook

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class WrapShaderBook(val extensionFunctions: Boolean = false)


interface ShaderBook
interface ShaderBookIndex<T : ShaderBook> {
    fun phraseSymbols(): Set<String>
    fun dynamicPhrase1Symbols(): Set<String>
    fun phrase(symbol: String): String?
    fun dynamicPhrase1(symbol: String, param0: String): String?
}