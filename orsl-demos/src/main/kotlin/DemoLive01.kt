import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.functions.symbol
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.gradient.functions.gradient
import org.openrndr.orsl.extension.noise.phrases.value13D
import org.openrndr.orsl.extension.sdf.functions.calcAO
import org.openrndr.orsl.extension.sdf.functions.hit
import org.openrndr.orsl.extension.sdf.functions.march
import org.openrndr.orsl.extension.sdf.functions.position
import org.openrndr.orsl.extension.sdf.phrases.sdBox


fun main() {
    application {
        configure {
            width = 800
            height = 800
        }
        oliveProgram {

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
                    var color by global<Vector3>()
                    val scene by function<Vector3, Double> {
                        val radius by 1.0
                        val coord0 by erot(it * Vector3(1.0, 1.2, 1.0), Vector3(1.0, 0.0, 0.0).normalized.symbol, it.z*0.3 + p_time)
                        val coord1 by erot(coord0 * Vector3(1.0, 1.2, 1.0), Vector3(0.0, 1.0, 0.0).normalized.symbol, it.y*0.3 + p_time)
                        val nb by value13D(coord1).x * 0.3
                        color = Vector3(abs(nb)*4.0, 0.0.symbol, -nb*4.0)

                        val d by sdBox(coord1, Vector3(3.0, 3.0, 3.0).symbol) + nb
                        val d2 by sdSphere(it, radius * 20.0)
                        min(d, -d2)
                    }

                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val aoCalcer by calcAO(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val n by 0.0
                    val c2 by (if_(result.hit) {
                        val normal by sceneNormal(result.position).normalized
                        normal.dot(Vector3(0.0, 0.0, 1.0)) * 0.5 + aoCalcer(result.position, normal) * 0.25
                    } else_ {
                        0.0.symbol
                    }) * (smoothstep(1.0, 0.0, (va_texCoord0-Vector2(0.5, 0.5)) .length))

                    //x_fill = Vector4(c2, c2, c2, 1.0)
                    x_fill = Vector4(color, 1.0)
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