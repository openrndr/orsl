package org.openrndr.extra.shadergenerator.dsl.shadestyle

import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.draw.font.BufferFlag
import org.openrndr.extra.shadergenerator.dsl.ShaderBuilder
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.extra.PbrFunctions
import org.openrndr.extra.shadergenerator.phrases.*
import org.openrndr.extra.shadergenerator.dsl.functions.FragmentDerivativeFunctions
import org.openrndr.draw.parameter as structParameter
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import kotlin.reflect.KProperty

open class ShadeStyleBuilder : ShaderBuilder(emptySet()), TransformPhrasesFunctions {
    class ParameterProperty<T>(val type: String, val register: (name: String) -> Unit = {}) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): ParameterProperty<T> {
            register(property.name.replace("p_", ""))
            return this
        }

        operator fun getValue(any: Any?, property: KProperty<*>) = symbol<T>(property.name, type)
    }

    class BufferProperty<T>(
        val type: String,
        val register: (name: String) -> Unit
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): BufferProperty<T> {
            register(property.name.replace("b_", ""))
            return this
        }

        operator fun getValue(any: Any?, property: KProperty<*>) = symbol<T>(property.name, type)
    }

    class ImageProperty<T : Image>(
        val type: String,
        val register: (name: String) -> Unit
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): ImageProperty<T> {
            register(property.name.replace("p_", ""))
            return this
        }

        operator fun getValue(any: Any?, property: KProperty<*>) = symbol<T>(property.name, type)
    }

    class ArrayParameter<T>(val type: String, val length: Int) {
        operator fun getValue(any: Any?, property: KProperty<*>) = arraySymbol<T>(property.name, length, type)
    }

    inline fun <reified T> parameter(): ParameterProperty<T> {
        return ParameterProperty(staticType<T>())
    }

    inline fun <reified T : Struct<T>> StyleParameters.parameter(value: T): ParameterProperty<T> {
        return ParameterProperty(staticType<T>()) { name ->
            (this@parameter).structParameter<T>(name.replace("p_", ""), value)
        }
    }

    inline fun <reified T : Struct<T>> StyleParameters.parameter(value: Array<T>): ParameterProperty<T> {
        return ParameterProperty(staticType<T>()) { name ->
            (this@parameter).structParameter<T>(name.replace("p_", ""), value)
        }
    }

    fun StyleParameters.parameter(value: Vector3): ParameterProperty<Vector3> {
        return ParameterProperty(staticType<Vector3>()) { name ->
            (this@parameter).parameter(name.replace("p_", ""), value)
        }
    }

    fun StyleParameters.parameter(value: Double): ParameterProperty<Double> {
        return ParameterProperty(staticType<Double>()) { name ->
            (this@parameter).parameter(name.replace("p_", ""), value)
        }
    }


    inline fun <reified T : Struct<T>> StyleBufferBindings.buffer(
        access: BufferAccess = BufferAccess.READ_WRITE,
        vararg flags: BufferFlag
    ): BufferProperty<T> {
        return BufferProperty(staticType<T>()) { name ->
            registerStructuredBuffer<T>(name, flags.toSet(), access)
        }
    }

    inline fun <reified T : Struct<T>> StyleBufferBindings.buffer(
        structuredBuffer: StructuredBuffer<T>,
        access: BufferAccess = BufferAccess.READ_WRITE,
        vararg flags: BufferFlag
    ): BufferProperty<T> {
        return BufferProperty(staticType<T>()) { name ->
            registerStructuredBuffer<T>(name, flags.toSet(), access)
            this@buffer.buffer(name, structuredBuffer)
        }
    }


    /**
     * Atomic counter buffer with default binding
     */
    fun StyleBufferBindings.buffer(
        atomicCounterBuffer: AtomicCounterBuffer,
        access: BufferAccess = BufferAccess.READ_WRITE,
        vararg flags: BufferFlag
    ): BufferProperty<AtomicCounterBuffer> {
        return BufferProperty(staticType<AtomicCounterBuffer>()) { name ->
            this@buffer.buffer(name, atomicCounterBuffer)
        }
    }

    inline fun <reified T : Image> StyleImageBindings.image(access: ImageAccess = ImageAccess.READ_WRITE): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access)
        }
    }

    inline fun <reified T : Image2D> StyleImageBindings.image(
        colorBuffer: ColorBuffer,
        level: Int,
        access: ImageAccess = ImageAccess.READ_WRITE
    ): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access)
            this@image.image(name, colorBuffer.imageBinding(level, access))
        }
    }

    inline fun <reified T : Image3D> StyleImageBindings.image(
        volumeTexture: VolumeTexture,
        level: Int,
        access: ImageAccess = ImageAccess.READ_WRITE
    ): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access)
            this@image.image(name, volumeTexture.imageBinding(level, access))
        }
    }



    inline fun <reified T> arrayParameter(length: Int): ArrayParameter<T> {
        return ArrayParameter(staticType<T>(), length)
    }


    val c_boundsPosition by parameter<Vector3>()
    val c_boundsSize by parameter<Vector3>()
    val c_screenPosition by parameter<Vector2>()
    val c_contourPosition by parameter<Double>()
    val c_instance by parameter<Int>()
    val c_element by parameter<Int>()
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


class FragmentTransformBuilder() : ShadeStyleBuilder(), HashPhrasesFunctions, ValueNoiseDerPhrasesFunctions,
    SimplexPhrasesFunctions, SdfPhrasesFunctions, FibonacciPhrasesFunctions, PbrFunctions, FragmentDerivativeFunctions {
    var color by output<Vector4>()
    var x_fill by output<Vector4>()
    var x_stroke by output<Vector4>()

    val v_worldNormal by parameter<Vector3>()
    val v_viewNormal by parameter<Vector3>()
    val v_worldPosition by parameter<Vector3>()
    val v_viewPosition by parameter<Vector3>()
    val v_clipPosition by parameter<Vector4>()

    val u_viewMatrix by parameter<Matrix44>()
    val u_modelMatrix by parameter<Matrix44>()
    val u_modelNormalMatrix by parameter<Matrix44>()
    val u_viewNormalMatrix by parameter<Matrix44>()

}
