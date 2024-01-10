import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.math.Vector4
import org.openrndr.orsl.shadergenerator.compute.computeTransform
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.get

const val STRUCT_LEN = 500

class MyStruct : Struct<MyStruct>() {
    val positions by arrayField<Vector4>(STRUCT_LEN)
}

val Symbol<MyStruct>.positions by MyStruct::positions[STRUCT_LEN]

/**
 * A compute demo in which a [computeStyle] is used to generate positions in
 * a buffer structured by [MyStruct]. A shade style is used to retrieve the
 * positions in the vertex transform.
 */
fun main() {
    application {
        program {
            val myStruct1 = MyStruct()
            val buffer1 = structuredBuffer(myStruct1)

            @Suppress("LocalVariableName")
            val updateBuffer = computeStyle {
                computeTransform {
                    val p_seconds by parameter<Double>()
                    val b_buffer by parameter<MyStruct>()

                    val id by c_giid.x

                    // spherical coordinates
                    val r by 5.0 + sin(id.double * 0.001) * 4.0
                    val a by id.double * 0.1000 + p_seconds
                    val b by id.double * 0.0201 + sin(a)

                    b_buffer.positions[id.int] = Vector4(
                        r * sin(a) * cos(b),
                        r * sin(a) * sin(b),
                        r * cos(a),
                        1.0
                    )
                }
            }
            updateBuffer.buffer("buffer", buffer1)

            val style = shadeStyle {
                vertexTransform {
                    val b_buffer by parameter<MyStruct>()
                    val pos by b_buffer.positions[c_instance]
                    x_position += pos.xyz
                }
                fragmentTransform {
                    val z by v_viewPosition.z
                    val nz by v_viewNormal.z

                    x_fill = Vector4(
                        nz * (sin(z * 0.9 + 1.0) * 0.5 + 0.5),
                        nz * (sin(z * 0.8 + 1.2) * 0.5 + 0.5),
                        nz * (sin(z * 0.7 + 1.4) * 0.5 + 0.5),
                        1.0.symbol
                    )
                }
            }
            style.buffer("buffer", buffer1)

            val sphere = sphereMesh(radius = 0.1)

            extend(Orbital())
            extend {
                // Run the compute shader to update buffer1
                updateBuffer.parameter("seconds", seconds)
                updateBuffer.execute(STRUCT_LEN, 1, 1)

                // Render making use of buffer1
                drawer.isolated {
                    drawer.shadeStyle = style
                    drawer.vertexBufferInstances(listOf(sphere), emptyList(), DrawPrimitive.TRIANGLES, STRUCT_LEN)
                }
            }
        }
    }
}
