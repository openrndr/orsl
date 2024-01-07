package org.openrndr.orsl.shadergenerator.dsl


interface Symbol<T> {
    val name: String
    val type: String
}

interface IfSymbol<T>: Symbol<T>

/**
 * Allow for else_ expressions on doIf
 */
interface DoIfSymbol: Symbol<Unit>


fun <T> symbol(name: String, type: String) = object : Symbol<T> {
    override val name = name
    override val type = type
}

inline fun <reified T> symbol(name: String) = object : Symbol<T> {
    override val name = name
    override val type = staticType<T>()
}

inline fun <reified T> ifSymbol(name: String) = object : IfSymbol<T> {
    override val name = name
    override val type = staticType<T>()
}


interface ArraySymbol<T> {
    val name: String
    val length: Int
    val type: String
}

fun <T> arraySymbol(name: String, length: Int, type: String) = object : ArraySymbol<T> {
    override val name = name
    override val length = length
    override val type = type
}

inline fun <reified T> arraySymbol(name: String, length: Int) = object : ArraySymbol<T> {
    override val name = name
    override val length = length
    override val type = staticType<T>()
}

data class Function0Symbol<R>(
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            return function
        }
}


data class FunctionSymbol1<T0, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            return function.replace("$0", x0)
        }
}

inline fun <reified T0, reified R> functionSymbol(v0: T0, function: String): Symbol<R> =
    FunctionSymbol1<T0, R>(v0 = v0, function = function, type = staticType<R>())

inline fun <reified T0, reified R> functionSymbol(p0: Symbol<T0>, function: String): Symbol<R> =
    FunctionSymbol1<T0, R>(p0 = p0, function = function, type = staticType<R>())


data class Function2Symbol<T0, T1, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val function: String,
    override val type: String

) : Symbol<R> {

    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1)
        }
}

inline fun <reified T0, reified T1, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    function: String
): Symbol<R> =
    Function2Symbol(p0 = p0, p1 = p1, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified R> functionSymbol(p0: Symbol<T0>, v1: T1, function: String): Symbol<R> =
    Function2Symbol(p0 = p0, v1 = v1, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified R> functionSymbol(v0: T0, p1: Symbol<T1>, function: String): Symbol<R> =
    Function2Symbol(v0 = v0, p1 = p1, function = function, type = staticType<R>())


data class Function3Symbol<T0, T1, T2, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val v2: T2? = null,
    val p2: Symbol<T2>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no v")
            val x1 = p1?.name ?: glsl(v1) ?: error("no v")
            val x2 = p2?.name ?: glsl(v2) ?: error("no v")
            return function.replace("$0", x0).replace("$1", x1).replace("$2", x2)
        }
}

inline fun <reified T0, reified T1, reified T2> Generator.functionCall(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    p2: Symbol<T2>,
    function: String
) {
    val b = Function3Symbol<T0, T1, T2, Nothing>(p0 = p0, p1 = p1, p2 = p2, function = function, type = "")
    emit("${b.name};")
}


inline fun <reified T0, reified T1, reified T2, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    p2: Symbol<T2>,
    function: String
): Symbol<R> =
    Function3Symbol(p0 = p0, p1 = p1, p2 = p2, function = function, type = staticType<R>())



inline fun <reified T0, reified T1, reified T2, reified R> functionSymbol(
    v0: T0,
    v1: T1,
    p2: Symbol<T2>,
    function: String
): Symbol<R> =
    Function3Symbol(v0 = v0, v1 = v1, p2 = p2, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified T2, reified R> functionSymbolSSV(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    v2: T2,
    function: String
): Symbol<R> =
    Function3Symbol(p0 = p0, p1 = p1, v2 = v2, function = function, type = staticType<R>())


inline fun <reified T0, reified T1, reified T2, reified R> functionSymbolSVV(
    p0: Symbol<T0>,
    v1: T1,
    v2: T2,
    function: String
): Symbol<R> =
    Function3Symbol(p0 = p0, v1 = v1, v2 = v2, function = function, type = staticType<R>())


data class Function4Symbol<T0, T1, T2, T3, R>(
    val v0: T0? = null,
    val p0: Symbol<T0>? = null,
    val v1: T1? = null,
    val p1: Symbol<T1>? = null,
    val v2: T2? = null,
    val p2: Symbol<T2>? = null,
    val v3: T3? = null,
    val p3: Symbol<T3>? = null,
    val function: String,
    override val type: String
) : Symbol<R> {
    override val name: String
        get() {
            val x0 = p0?.name ?: glsl(v0) ?: error("no x0")
            val x1 = p1?.name ?: glsl(v1) ?: error("no x1")
            val x2 = p2?.name ?: glsl(v2) ?: error("no x2")
            val x3 = p3?.name ?: glsl(v3) ?: error("no x3")
            return function.replace("$0", x0).replace("$1", x1).replace("$2", x2).replace("$3", x3)
        }
}

inline fun <reified T0, reified T1, reified T2, reified T3, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    p2: Symbol<T2>,
    p3: Symbol<T3>,
    function: String
): Symbol<R> =
    Function4Symbol(p0 = p0, p1 = p1, p2 = p2, p3 = p3, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified T2, reified T3, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    p2: Symbol<T2>,
    v3: T3,
    function: String
): Symbol<R> =
    Function4Symbol(p0 = p0, p1 = p1, p2 = p2, v3 = v3, function = function, type = staticType<R>())

inline fun <reified T0, reified T1, reified T2, reified T3, reified R> functionSymbol(
    p0: Symbol<T0>,
    p1: Symbol<T1>,
    v2: T2,
    v3: T3,
    function: String
): Symbol<R> =
    Function4Symbol(p0 = p0, p1 = p1, v2 = v2, v3 = v3, function = function, type = staticType<R>())
