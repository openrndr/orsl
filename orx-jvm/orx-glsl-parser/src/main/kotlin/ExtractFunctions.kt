package org.openrndr.extra.glslparser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.openrndr.extra.glslparser.antlr.GLSLLexer
import org.openrndr.extra.glslparser.antlr.GLSLParser
import org.openrndr.extra.glslparser.antlr.GLSLParserBaseListener

data class GLSLFunction(val name: String, val returnType: String, val parameters: List<Pair<String, String>>)

class FunctionExtractor() : GLSLParserBaseListener() {
    val functions = mutableListOf<GLSLFunction>()
    override fun enterFunction_prototype(ctx: GLSLParser.Function_prototypeContext?) {
        val returnType = ctx!!.fully_specified_type().type_specifier().text

        val functionName = ctx.IDENTIFIER().text
        val params = ctx.function_parameters()?.parameter_declaration()?.map {
            val name = it.parameter_declarator().IDENTIFIER().text
            val type = it.parameter_declarator().type_specifier().type_specifier_nonarray().text
            name to type
        } ?: emptyList()
        functions.add(GLSLFunction(functionName, returnType, params))
        super.enterFunction_prototype(ctx)
    }
}

fun extractFunctions(glslSource:String) : List<GLSLFunction> {
    val lexer = GLSLLexer(CharStreams.fromString(glslSource))
    val tokens = CommonTokenStream(lexer)
    val parser = GLSLParser(tokens)
    val fe = FunctionExtractor()
    ParseTreeWalker.DEFAULT.walk(fe, parser.translation_unit())
    return fe.functions
}


fun main() {
    val glslSource = """|float hash22(vec2 x) {
                |    return vec2(4.0);
                |}
            """.trimMargin()

    println(extractFunctions(glslSource))
}