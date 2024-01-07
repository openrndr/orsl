import org.junit.jupiter.api.Test
import org.openrndr.draw.Struct
import org.openrndr.draw.structToShaderStorageFormat
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

class TestStructSSBOFormat {
    @Test
    fun testSimpleConversion() {
        class MyStruct: Struct<MyStruct>() {
            var position by field<Vector3>()
            var floatField by field<Double>()
            var intField by field<Int>()
            var matrix44Field by field<Matrix44>()
            var vec3Array by arrayField<Vector3>(100)
        }

        val format = structToShaderStorageFormat(MyStruct())

    }

    @Test
    fun testComplexConversion() {

        class SomeStruct : Struct<SomeStruct>() {
            var vec4Field by field<Vector4>()
        }

        class MyStruct: Struct<MyStruct>() {
            var position by field<Vector3>()
            var floatField by field<Double>()
            var intField by field<Int>()
            var matrix44Field by field<Matrix44>()
            var vec3Array by arrayField<Vector3>(100)
            var structField by field<SomeStruct>()
        }

        val myStruct = MyStruct()
        myStruct.structField = SomeStruct()
        val format = structToShaderStorageFormat(myStruct)
    }

}