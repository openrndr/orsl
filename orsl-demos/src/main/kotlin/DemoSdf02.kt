import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.functions.Functions
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.orsl.shadergenerator.dsl.structs.setValue
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.gradient.functions.gradient
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
                    val va_texCoord0 by parameter<Vector2>()
                    val p_viewMatrix by parameter<Matrix44>()
                    val fixedCoord = Vector2(va_texCoord0.x, 1.0 - va_texCoord0.y)

                    val rayDir by (p_viewMatrix * Vector4(fixedCoord - Vector2(0.5), -1.0, 0.0)).xyz.normalized
                    val rayOrigin by p_origin

                    val sdSphere by function<Vector3, Double, Double> { p, s ->
                        p.length - s
                    }

                    val scene by function<Vector3, Double> {
                        val radius by 1.0
                        val fx by floor(it.x + 0.5)
                        val fy by floor(it.y + 0.5)
                        val fv by Vector2(fx, fy)
                        val zOff by cos(fv.length * 0.25 + p_time) * 4.0

                        val coord0 = Vector3(
                            (it.x + 0.5).mod(1.0) - 0.5,
                            (it.y + 0.5).mod(1.0) - 0.5,
                            it.z + zOff
                        )
                        val d by sdSphere(coord0, radius * 0.5)
                        val d2 by sdSphere(it, radius * 50.0)
                        min(d, -d2)
                    }

                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val n by 0.0
                    val c2 by n.elseIf(result.hit) {
                        val normal by sceneNormal(result.position).normalized
                        normal.dot(Vector3(0.0, 0.0, 1.0))
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