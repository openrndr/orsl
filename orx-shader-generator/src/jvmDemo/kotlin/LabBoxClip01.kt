import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import kotlin.math.abs

fun main() {
    application {
        program {

            extend {
                val normal = (mouse.position - drawer.bounds.center).xy0.normalized

                fun clipToBox(n: Vector3) : Vector3 {
                    val an = Vector3(abs(n.x), abs(n.y), abs(n.z))
                    val normalized =
                    if (an.x >= an.y && an.x >= an.z) {
                        n / an.x
                        //Vector3(n.x/ an.x, n.y/an.x, n.z/an.x)
                    } else if (an.y >= an.x && an.y >= an.z) {
                        //Vector3(n.x/ an.y, n.y/an.y, n.z/an.y)
                        n / an.y
                    } else {
                        //Vector3(n.x/ an.z, n.y/an.z, n.z/an.z)
                        n / an.z
                    }
                    return normalized

                }


                val boxed = clipToBox(normal)
                drawer.stroke = ColorRGBa.RED

                drawer.lineSegment(drawer.bounds.center, drawer.bounds.center + boxed.xy * 40.0)

                drawer.stroke = ColorRGBa.GREEN

                drawer.lineSegment(drawer.bounds.center, drawer.bounds.center + normal.xy * 40.0)

            }
        }
    }
}