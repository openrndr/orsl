package org.openrndr.extra.shadergenerator.dsl.shadestyle

import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.phrases.HashPhrasesFunctions
import org.openrndr.extra.shadergenerator.phrases.SimplexPhrasesFunctions
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.reflect.KProperty

open class ShadeStyleBuilder : ShaderBuilder() {
    class Parameter<T>(val type: String) {
        operator fun getValue(any: Any?, property: KProperty<*>) = symbol<T>(property.name, type)
    }

    class ArrayParameter<T>(val type: String, val length: Int) {
        operator fun getValue(any: Any?, property: KProperty<*>) = arraySymbol<T>(property.name, length, type)
    }

    inline fun <reified T> parameter(): Parameter<T> {
        return Parameter(staticType<T>())
    }

    inline fun <reified T> arrayParameter(length: Int): ArrayParameter<T> {
        return ArrayParameter(staticType<T>(), length)
    }


    val c_boundsPosition by variable<Vector3>()
    val c_boundsSize by variable<Vector3>()
    val c_screenPosition by variable<Vector2>()
    val c_contourPosition by variable<Double>()
    val c_instance by variable<Int>()
    val c_element by variable<Int>()
}


class VertexTransformBuilder() : ShadeStyleBuilder(), HashPhrasesFunctions, SimplexPhrasesFunctions {
    var x_position by output<Vector3>()
    var x_normal by output<Vector3>()
    var x_viewMatrix by output<Matrix44>()
    var x_modelMatrix by output<Matrix44>()
    var x_modelNormalMatrix by output<Matrix44>()
    var x_viewNormalMatrix by output<Matrix44>()
    var x_projectionMatrix by output<Matrix44>()
}


class FragmentTransformBuilder() : ShadeStyleBuilder(), HashPhrasesFunctions,
    SimplexPhrasesFunctions {
    var color by output<Vector4>()
    var x_fill by output<Vector4>()
    var x_stroke by output<Vector4>()

    val v_worldNormal by variable<Vector3>()
    val v_viewNormal by variable<Vector3>()
    val v_worldPosition by variable<Vector3>()
    val v_viewPosition by variable<Vector3>()
    val v_clipPosition by variable<Vector4>()

    val v_viewMatrix by variable<Matrix44>()
    val v_modelMatrix by variable<Matrix44>()
    val v_modelNormalMatrix by variable<Matrix44>()
    val v_viewNormalMatrix by variable<Matrix44>()

}
