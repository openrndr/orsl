//package org.openrndr.orsl.shadergenerator.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.impl.kotlin.KSPropertyDeclarationImpl
import org.jetbrains.kotlin.psi.psiUtil.children
import org.openrndr.orsl.glslparser.GLSLFunction
import org.openrndr.orsl.glslparser.extractFunctions

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

/**
 * Return a type name used in @JvmName()
 */
internal fun glslToJvmName(glslType:String) : String {
    return when(glslType) {
        "float" -> "Sd"
        "vec2" -> "Sv2"
        "vec3" -> "Sv3"
        "vec4" -> "Sv4"
        "mat2" -> "Sm22"
        "mat3" -> "Sm33"
        "mat4" -> "Sm44"
        "int" -> "Si"
        "bool" -> "Sb"
        else -> error("unsupported glsl type '$glslType'")
    }
}

class ShaderFunctionProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val shaderBookSymbols = resolver.getSymbolsWithAnnotation("$annotationPackage.WrapShaderBook")
        val ret = shaderBookSymbols.filter { !it.validate() }.toList()
        shaderBookSymbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(ShaderBookVisitor(it.annotations.filter { println(it.shortName.asString()); it.shortName.asString() == "WrapShaderBook" }.first().arguments.first().value == true) , Unit) }

        return ret
    }

    inner class ShaderBookVisitor(val generateExtensionFunctions: Boolean = false): KSVisitorVoid() {
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

                val receiver = if (generateExtensionFunctions) { "ShaderBuilder." } else ""
                val jvmName = """@JvmName("${name}${parameters.joinToString("") { glslToJvmName(it.second) }}")"""

                return when (parameters.size) {
                    1 -> """$jvmName
                    |fun $receiver${name}($kotlinFunctionParameters): FunctionSymbol1<$parameterTypes, ${glslToKotlin(returnType)}> {
                    |    emitPreamble("#pragma import $name")
                    |    return FunctionSymbol1(p0 = ${parameters[0].first}, function = "$name($0)", type = "$returnType")  
                    |} 
                    """
                    else -> """$jvmName
                    |fun $receiver${name}($kotlinFunctionParameters): Symbol<${glslToKotlin(returnType)}> {
                    |    emitPreamble("#pragma import $name")
                    |    return object : Symbol<${glslToKotlin(returnType)}> {
                    |         override val name = "$name($kotlinFunctionArguments)"
                    |         override val type = "$returnType";
                    |    }    
                    |}
                    """
                }
            }

            file.appendText("package $packageName\n\n")
            file.appendText("import $annotationPackage.ShaderBookIndex\n\n")
            file.appendText("import kotlin.reflect.KFunction2\n")
            file.appendText("import org.openrndr.math.*\n")
            file.appendText("import org.openrndr.orsl.shadergenerator.dsl.FunctionSymbol1\n")
            file.appendText("import org.openrndr.orsl.shadergenerator.dsl.Generator\n")
            file.appendText("import org.openrndr.orsl.shadergenerator.dsl.Symbol\n")
            file.appendText("import kotlin.jvm.JvmName\n")
            if (generateExtensionFunctions) {
                file.appendText("import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder\n")
            }

            if (!generateExtensionFunctions) {
                file.appendText("""@Suppress("INAPPLICABLE_JVM_NAME")
                |interface ${generatedClassName}: Generator  {
                |${functionsFromProperties.joinToString("\n") { it.toKotlin() }.prependIndent("    ")}
                |}""".trimMargin()
                )
            } else {
                file.appendText("${functionsFromProperties.joinToString("\n") { it.toKotlin() }}".trimMargin())
            }
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