import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.gradient.functions.gradient
import org.openrndr.orsl.extension.sdf.functions.calcAO
import org.openrndr.orsl.extension.sdf.functions.hit
import org.openrndr.orsl.extension.sdf.functions.march
import org.openrndr.orsl.extension.sdf.functions.position


fun main() {
    application {
        configure {
            width = 800
            height = 800
        }
        program {

            val ss = shadeStyle {
                fragmentTransform {
                    val p_origin by parameter<Vector3>()
                    val p_time by parameter<Double>()
                    val va_texCoord0 by varyingIn<Vector2>()

                    val p_viewMatrix by parameter<Matrix44>()
                    val fixedCoord = Vector2(va_texCoord0.x, 1.0 - va_texCoord0.y)

                    val rayDir by (p_viewMatrix * Vector4(fixedCoord - Vector2(0.5), -1.0, 0.0)).xyz.normalized
                    val rayOrigin by p_origin

                    val sdSphere by function<Vector3, Double, Double> { p, s ->
                        p.length - s
                    }

                    val scene by function<Vector3, Double> {
                        val radius by 1.0
                        val coord0 = it + Vector3(
                            cos(it.z * 5.432 + p_time * 2.2),
                            cos(it.x * 10.0 - p_time),
                            sin(it.y * 8.43 + p_time)
                        ) * 0.3

                        val d by sdSphere(coord0, radius)
                        val d2 by sdSphere(it, radius * 50.0)
                        min(d, -d2)
                    }

                    var b by variable(5.0)


                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val aoCalcer by calcAO(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val n by 0.0

                    doIf(result.hit) {
                        b = 1.0.symbol
                    } else_ {
                        b = 0.0.symbol
                    }

                    val c2 by n.elseIf(result.hit) {
                        val normal by sceneNormal(result.position).normalized
                        normal.dot(Vector3(0.0, 0.0, 1.0)) * 0.5 + aoCalcer(result.position, normal) * 0.25
                    }

                    x_fill = Vector4(c2, c2, c2, 1.0)
                }
                parameter("time", seconds)

            }
            val o = extend(Orbital())
            extend {
                ss.parameter("viewMatrix", normalMatrix(o.camera.viewMatrix().inversed))
                ss.parameter("origin", (o.camera.viewMatrix().inversed * Vector4.UNIT_W).xyz)
                ss.parameter("time", seconds)
                drawer.defaults()
                drawer.shadeStyle = ss
                drawer.rectangle(drawer.bounds)
            }
        }
    }
}