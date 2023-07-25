import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.structs.get


class AcbTestStruct: Struct<AcbTestStruct>() {
    val floats by arrayField<Double>(100)
}
val Symbol<AcbTestStruct>.floats by AcbTestStruct::floats[100]

fun main() {
    application {
        program {
            val struct = AcbTestStruct()
            val sb = structuredBuffer(struct)
            val acb = AtomicCounterBuffer.create(1)
            //acb.write(intArrayOf(1337))

            val cs = computeStyle {
                computeTransform {
                    val b_sb by buffer(sb)
                    val b_acb by parameter<AtomicCounterBuffer>()
                    val x by b_sb.floats[0]
                    val inc by b_acb.increment(0)
                }
                buffer("acb", acb)
                buffer("sb", sb)
            }
            cs.execute(1,1,1)

            val values = acb.read()
            println(values[0])
        }
    }
}