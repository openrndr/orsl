import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.impl.kotlin.KSPropertyDeclarationImpl
import org.jetbrains.kotlin.psi.psiUtil.children
import org.openrndr.extra.glslparser.GLSLFunction
import org.openrndr.extra.glslparser.extractFunctions

fun glslToKotlin(glslType:String) : String {
    return when(glslType) {
        "float" -> "Double"
        "vec2" -> "Vector2"
        "vec3" -> "Vector3"
        "vec4" -> "Vector4"
        "mat2" -> "Matrix22"
        "mat3" -> "Matrix33"
        "mat4" -> "Matrix44"
        "int" -> "Int"
        "bool" -> "Boolean"
        else -> error("unsupported glsl type '$glslType'")
    }
}

class ShaderFunctionProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val shaderBookSymbols = resolver.getSymbolsWithAnnotation("$annotationPackage.WrapShaderBook")
        val ret = shaderBookSymbols.filter { !it.validate() }.toList()
        shaderBookSymbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(ShaderBookVisitor(), Unit) }

        return ret
    }

    inner class ShaderBookVisitor: KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = "${classDeclaration.simpleName.asString()}"
            val generatedClassName = "${className}Functions"
            val file = codeGenerator.createNewFile(Dependencies(true, classDeclaration.containingFile!!), packageName , className+ "Functions")


            val functionsFromProperties = classDeclaration.getDeclaredProperties().map {
                val k = it as KSPropertyDeclarationImpl
                val glsl = k.ktProperty.node.lastChildNode.children().toList().drop(1).dropLast(1).joinToString("") { it.text }
                extractFunctions(glsl).first()
            }.toList()



            fun GLSLFunction.toKotlin() : String {
                val kotlinFunctionParameters = parameters.joinToString(", ") {
                    "${it.first}: Symbol<${glslToKotlin(it.second)}>"
                }
                val parameterTypes = parameters.joinToString(", ") { glslToKotlin(it.second) }

                val kotlinFunctionArguments = parameters.joinToString(", ") {
                    "${"$"}{${it.first}.name}"
                }

                return when (parameters.size) {
                    1 -> """fun ${name}($kotlinFunctionParameters): FunctionSymbol1<$parameterTypes, ${glslToKotlin(returnType)}> {
                    |    emitPreamble("#pragma import $name")
                    |    return FunctionSymbol1(p0 = ${parameters[0].first}, function = "$name($0)")  
                    |} 
                    """
                    else -> """fun ${name}($kotlinFunctionParameters): Symbol<${glslToKotlin(returnType)}> {
                    |    emitPreamble("#pragma import $name")
                    |    return object : Symbol<${glslToKotlin(returnType)}> {
                    |         override val name = "$name($kotlinFunctionArguments)"
                    |    }    
                    |}
                    """
                }
            }

            file.appendText("package $packageName\n\n")
            file.appendText("import $annotationPackage.ShaderBookIndex\n\n")
            file.appendText("import kotlin.reflect.KFunction2\n")
            file.appendText("import org.openrndr.math.*\n")
            file.appendText("import org.openrndr.extra.shadergenerator.phrases.dsl.FunctionSymbol1\n")
            file.appendText("import org.openrndr.extra.shadergenerator.phrases.dsl.Generator\n")
            file.appendText("import org.openrndr.extra.shadergenerator.phrases.dsl.Symbol\n")

            file.appendText("""interface ${generatedClassName}: Generator  {
                |${functionsFromProperties.joinToString("\n") { it.toKotlin() }}
                |}""".trimMargin())
            file.close()
        }
    }
}

class ShaderFunctionProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return ShaderFunctionProcessor(environment.codeGenerator, environment.logger)
    }
}