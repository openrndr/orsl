import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.compute.execute
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.dsl.structs.get

class BufferStruct : Struct<BufferStruct>() {
    val floats by arrayField<Double>(640)
}

val Symbol<BufferStruct>.floats by BufferStruct::floats[640]

/**
 * A compute demo in which a compute style is used to generate positions in
 * a buffer structured by [BufferStruct]. A shade style is used to retrieve the
 * positions in the vertex transform.
 */
fun main() {
    application {
        program {
            val bufferStruct = BufferStruct()
            val buffer = structuredBuffer(bufferStruct)

            val cs = computeStyle {
                computeTransform {
                    val b_buffer by parameter<BufferStruct>()
                    b_buffer.floats[c_giid.x.int] = c_giid.x.double
                }
            }

            cs.buffer("buffer", buffer)
            cs.execute(640, 1, 1)

            val ss = shadeStyle {
                vertexTransform {
                    val b_buffer by parameter<BufferStruct>()
                    val x by b_buffer.floats[c_instance] * 1.5
                    x_position += Vector3(x, 0.0.symbol, 0.0.symbol)
                }
                fragmentTransform {
                    x_fill = Vector4(v_viewNormal.z, v_viewNormal.z, v_viewNormal.z, 1.0.symbol)
                }
            }
            ss.buffer("buffer", buffer)

            val sphere = sphereMesh()
            extend(Orbital())
            extend {
                drawer.isolated {
                    drawer.shadeStyle = ss
                    drawer.vertexBufferInstances(listOf(sphere), emptyList(), DrawPrimitive.TRIANGLES, 640)
                }
            }
        }
    }
}