import org.junit.jupiter.api.Test
import org.openrndr.draw.*
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.dsl.structs.get
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.Circle

class TestStruct : Struct<TestStruct>() {
    var v3 by field<Vector3>()
    var v3a by arrayField<Vector3>(4)
}

val Symbol<TestStruct>.v3 by TestStruct::v3
val Symbol<TestStruct>.v3a by TestStruct::v3a[4]

class TestStructSupport : AbstractApplicationTestFixture() {
    @Test
    fun structSupportForPrimitives() {
        val struct = TestStruct()
        struct.v3 = Vector3.ONE
        struct.v3a = arrayOf(Vector3.UNIT_X, Vector3.UNIT_Y, Vector3.UNIT_Z, Vector3.ONE)
        val ss = shadeStyle {
            vertexTransform {
                val p_struct by parameter<TestStruct>()
                val b by p_struct
                val a by b.v3a
                val a0 by a[0]
            }

            fragmentTransform {
                val p_struct by parameter<TestStruct>()
                x_fill = Vector4(p_struct.v3, 1.0)
                x_fill = Vector4(p_struct.v3a[0], 1.0)

            }
            parameter("struct", struct)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(Vector2(0.0, 0.0), 40.0)
        program.drawer.rectangle(program.drawer.bounds)
        program.drawer.point(program.drawer.bounds.center)
        program.drawer.contour(Circle(program.drawer.bounds.center, 100.0).contour)
        program.drawer.shape(Circle(program.drawer.bounds.center, 100.0).shape)
        program.drawer.lineSegment(program.drawer.bounds.position(0.0, 0.0), program.drawer.bounds.position(1.0, 1.0))
        val sphere = sphereMesh()
        program.drawer.vertexBuffer(sphere, DrawPrimitive.TRIANGLES)
    }

    @Test
    fun arrayOfstructSupportForPrimitives() {
        val struct = TestStruct()
        struct.v3 = Vector3.ONE
        struct.v3a = arrayOf(Vector3.UNIT_X, Vector3.UNIT_Y, Vector3.UNIT_Z, Vector3.ONE)

        val structs = arrayOf(struct, struct, struct, struct)

        val ss = shadeStyle {
            vertexTransform {
                val p_structs by arrayParameter<TestStruct>(4)
                val b by p_structs[0]
                val a by b.v3a
                val a0 by a[0]
            }

            fragmentTransform {
                val p_structs by arrayParameter<TestStruct>(4)
                x_fill = Vector4(p_structs[0].v3, 1.0)
                x_fill = Vector4(p_structs[0].v3a[0], 1.0)

            }
            parameter("structs", structs)
        }
        program.drawer.shadeStyle = ss
        program.drawer.circle(Vector2(0.0, 0.0), 40.0)
        program.drawer.rectangle(program.drawer.bounds)
        program.drawer.point(program.drawer.bounds.center)
        program.drawer.contour(Circle(program.drawer.bounds.center, 100.0).contour)
        program.drawer.shape(Circle(program.drawer.bounds.center, 100.0).shape)
        program.drawer.lineSegment(program.drawer.bounds.position(0.0, 0.0), program.drawer.bounds.position(1.0, 1.0))
        val sphere = sphereMesh()
        program.drawer.vertexBuffer(sphere, DrawPrimitive.TRIANGLES)
    }
}