import org.openrndr.application
import org.openrndr.draw.AtomicCounterBuffer

fun main() {
    application {
        program {
            val acb = AtomicCounterBuffer.create(1)
            acb.write(intArrayOf(1337))

            val values = acb.read()
            println(values[0])
        }
    }
}