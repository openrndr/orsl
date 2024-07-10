package org.openrndr.orsl.shadergenerator.dsl.shadestyle

import org.openrndr.draw.*
import org.openrndr.draw.font.BufferAccess
import org.openrndr.draw.font.BufferFlag
import org.openrndr.orsl.shadergenerator.dsl.ShaderBuilder
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.phrases.*
import org.openrndr.orsl.shadergenerator.dsl.functions.FragmentDerivativeFunctions
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

    class ArrayImageProperty<T : Image>(
        val type: String,
        val length: Int,
        val register: (name: String) -> Unit
    ) {
        operator fun provideDelegate(any: Any?, property: KProperty<*>): ArrayImageProperty<T> {
            register(property.name.replace("p_", ""))
            return this
        }

        operator fun getValue(any: Any?, property: KProperty<*>) = arraySymbol<T>(property.name, length, type)
    }




    class ArrayParameter<T>(val type: String, val length: Int) {
        operator fun getValue(any: Any?, property: KProperty<*>) = arraySymbol<T>(property.name, length, type)
    }

    inline fun <reified T> parameter(): ParameterProperty<T> {
        return ParameterProperty(staticType<T>())
    }


    inline fun <reified T> vertexAttribute(): ParameterProperty<T> {
        return ParameterProperty(staticType<T>())
    }

    inline fun <reified T> instanceAttribute(): ParameterProperty<T> {
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
            registerStructuredBuffer<T>(name, access, flags.toSet())
        }
    }

    inline fun <reified T : Struct<T>> StyleBufferBindings.buffer(
        structuredBuffer: StructuredBuffer<T>,
        access: BufferAccess = BufferAccess.READ_WRITE,
        vararg flags: BufferFlag
    ): BufferProperty<T> {
        return BufferProperty(staticType<T>()) { name ->
            registerStructuredBuffer<T>(name, access, flags.toSet())
            this@buffer.buffer(name, structuredBuffer.ssbo)
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

    inline fun <reified T : Image> StyleImageBindings.image(access: ImageAccess = ImageAccess.READ_WRITE, flags: Set<ImageFlag> = emptySet()): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access, flags)
        }
    }

    inline fun <reified T : Image2D> StyleImageBindings.image(
        colorBuffer: ColorBuffer,
        level: Int = 0,
        access: ImageAccess = ImageAccess.READ_WRITE,
        flags: Set<ImageFlag> = emptySet()
    ): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access, flags)
            this@image.image(name, colorBuffer, level)
        }
    }


    inline fun <reified T : Image2D> StyleImageBindings.images(
        colorBuffers: Array<ColorBuffer>,
        levels: Array<Int>,
        access: ImageAccess = ImageAccess.READ_WRITE,
        flags: Set<ImageFlag> = emptySet()
    ): ArrayImageProperty<T> {
        return ArrayImageProperty(staticType<T>(), colorBuffers.size) { name ->
            this@images.registerImageBinding(name, access, flags)
            this@images.image(name, colorBuffers, levels)
        }
    }

    inline fun <reified T : Image3D> StyleImageBindings.image(
        volumeTexture: VolumeTexture,
        level: Int,
        access: ImageAccess = ImageAccess.READ_WRITE,
        flags: Set<ImageFlag> = emptySet()
    ): ImageProperty<T> {
        return ImageProperty(staticType<T>()) { name ->
            this@image.registerImageBinding(name, access, flags)
            this@image.image(name, volumeTexture)
        }
    }

    inline fun <reified T : Image> StyleImageBindings.images(
        size: Int,
        access: ImageAccess = ImageAccess.READ_WRITE,
        flags: Set<ImageFlag> = emptySet()
    ): ArrayImageProperty<T> {
        return ArrayImageProperty(staticType<T>(), size) { name ->
            this@images.registerImageBinding(name, access, flags)
        }
    }

    /**
     * Reference to an already declared image binding
     */
    inline fun <reified T : Image> StyleImageBindings.images(): ArrayImageProperty<T> {
        return ArrayImageProperty(staticType<T>(), 0) { name ->
            require(imageAccess.containsKey(name.replace("p_", "")))
        }
    }

    inline fun <reified T : Image3D> StyleImageBindings.images(
        volumeTextures: Array<VolumeTexture>,
        levels: Array<Int>,
        access: ImageAccess = ImageAccess.READ_WRITE,
        flags: Set<ImageFlag> = emptySet()
    ): ArrayImageProperty<T> {
        return ArrayImageProperty(staticType<T>(), volumeTextures.size) { name ->
            this@images.registerImageBinding(name, access, flags)
            this@images.image(name, volumeTextures, levels)
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


class VertexTransformBuilder() : ShadeStyleBuilder(){
    var x_position by output<Vector3>()
    var x_normal by output<Vector3>()
    var x_viewMatrix by output<Matrix44>()
    var x_modelMatrix by output<Matrix44>()
    var x_modelNormalMatrix by output<Matrix44>()
    var x_viewNormalMatrix by output<Matrix44>()
    var x_projectionMatrix by output<Matrix44>()

    inline fun <reified T> varyingOut(): VaryingOutProperty<T> {
        val glslType = staticType<T>()
        return VaryingOutProperty(this@VertexTransformBuilder, glslType)
    }
}


class FragmentTransformBuilder() : ShadeStyleBuilder(), FragmentDerivativeFunctions {
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

    /**
     * declare a varying in
     */
    inline fun <reified T> varyingIn(forceDefinition: Boolean = false): VaryingInProperty<T> {
        val glslType = staticType<T>()
        return VaryingInProperty(this@FragmentTransformBuilder, glslType, forceDefinition)
    }
}
