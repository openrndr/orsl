import org.openrndr.application
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.execute

fun main() {
    application {
        program {
            val cs = computeStyle {
                computeTransform = """float a = p_leuk;"""
            }
            cs.parameter("leuk", 5.0)
            cs.execute(1, 1, 1)
        }
    }
}