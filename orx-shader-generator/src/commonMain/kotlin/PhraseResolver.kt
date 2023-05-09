package org.openrndr.extra.shadergenerator.phrases

import org.openrndr.extra.shadergenerator.annotations.ShaderBookIndex

class PhraseResolver {

    val functions = mutableMapOf<String, () -> String>()


    val indices = mutableListOf<ShaderBookIndex<*>>()
    fun resolvePhrase(symbol: String, param0: String?): String? {
        return if (param0 == null) {
            functions.get(symbol)?.invoke() ?: indices.firstNotNullOfOrNull { it.phrase(symbol) }
        } else {
            indices.firstNotNullOfOrNull { it.dynamicPhrase1(symbol, param0) }
        }
    }

    /**
     * Preprocess shader source.
     * Looks for "#pragma import" statements and injects found phrases.
     * @param source GLSL source code encoded as string
     * @return GLSL source code with injected shader phrases
     */
    fun preprocessShader(source: String, symbols: MutableSet<String> = mutableSetOf()): String {
        val lines = source.split("\n")
        val funcName = Regex("""^\s*#pragma\s+import\s+([a-zA-Z0-9_.]+)(\([a-zA-Z0-9_.]+\))?""")
        val processed = lines.map { line ->
            if (line.contains("#pragma")) {
                val search = funcName.find(line)
                val symbol = search?.groupValues?.get(1) ?: return@map line
                val param0 = search.groupValues.getOrNull(2)?.drop(1)?.dropLast(1)?.ifEmpty { null }

                val cacheSymbol = listOfNotNull(symbol, param0).joinToString("_")

                if (cacheSymbol !in symbols) {
                    symbols.add(cacheSymbol)
                    val registryPhrase =
                        resolvePhrase(symbol, param0) ?: error("symbol '$symbol' could not be resolved")
                    registryPhrase.let { preprocessShader(registryPhrase, symbols) }
                } else {
                    ""
                }
            } else {
                line
            }
        }
        return processed.joinToString("\n")
    }

}

