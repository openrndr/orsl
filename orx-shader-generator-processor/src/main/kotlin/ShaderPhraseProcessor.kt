import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

val annotationPackage = "org.openrndr.extra.shadergenerator.annotations"

class ShaderPhraseProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val shaderBookSymbols = resolver.getSymbolsWithAnnotation("$annotationPackage.IndexShaderBook")
        val ret = shaderBookSymbols.filter { !it.validate() }.toList()
        shaderBookSymbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(ShaderBookVisitor(), Unit) }

        return ret
    }

    inner class ShaderBookVisitor: KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = classDeclaration.simpleName.asString()
            val generatedClassName = "${className}Index"
            val file = codeGenerator.createNewFile(Dependencies(true, classDeclaration.containingFile!!), packageName , className)

            val propertyNames = classDeclaration.getDeclaredProperties().map {
                it.simpleName.asString()
            }

            val functionNames = classDeclaration.getDeclaredFunctions().mapNotNull {
                if (!it.isConstructor()) {
                    it.simpleName.asString()
                } else {
                    null
                }
            }

            file.appendText("package $packageName\n\n")
            file.appendText("import $annotationPackage.ShaderBookIndex\n\n")
            file.appendText("import kotlin.reflect.KFunction2\n")
            file.appendText("""class ${generatedClassName}(val book: $className) : ShaderBookIndex<$className> {
                |    private val m = mapOf(${propertyNames.joinToString(", ") { "\"${it}\" to $className::$it"}})
                |    private val d1 = mapOf<String, KFunction2<$className, String, String>>(${functionNames.joinToString(", ") { "\"${it}\" to $className::$it"}})
                |    override fun phraseSymbols(): Set<String> {
                |        return m.keys   
                |    } 
                |    override fun dynamicPhrase1Symbols(): Set<String> {
                |       return d1.keys
                |    }
                |    
                |    override fun phrase(symbol: String): String? {
                |        return m[symbol]?.get(book)
                |    }
                |    override fun dynamicPhrase1(symbol: String, param0: String): String? {
                |        return d1[symbol]?.invoke(book, param0)
                |    }
                |} """.trimMargin())
            file.close()
        }
    }
}

class ShaderPhraseProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return ShaderPhraseProcessor(environment.codeGenerator, environment.logger)
    }
}