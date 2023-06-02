import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.Functions
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.math.transforms.normalMatrix

class MarchResult : Struct<MarchResult>() {
    var position by field<Vector3>()
    var hit by field<Boolean>()
    var travel by field<Double>()
    var normal by field<Vector3>()
}

var Symbol<MarchResult>.position by MarchResult::position
var Symbol<MarchResult>.hit by MarchResult::hit
var Symbol<MarchResult>.travel by MarchResult::travel
var Symbol<MarchResult>.normal by MarchResult::normal

fun ShaderBuilder.march(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>
) = Functions.Function2PropertyProvider<Vector3, Vector3, _>(
    this@march, staticType<Vector3>(),
    staticType<Vector3>(),
    staticType<MarchResult>()
) { origin, direction ->
    val result by MarchResult()
    val False by false
    val True by true
    result.hit = False
    var position by variable(origin)

    val i by variable<Int>()
    i.for_(0 until 300) {
        val distance by scene(position)

        doIf(abs(distance) lt 1E-2) {
            result.hit = True
            result.position = position
            break_()
        }
        position += direction * distance * 0.5
    }
    result
}

fun ShaderBuilder.calcAO(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>
): Functions.Function2PropertyProvider<Vector3, Vector3, Double> {
    return Functions.Function2PropertyProvider(
        this@calcAO, staticType<Vector3>(),
        staticType<Vector3>(),

        staticType<Double>()
    ) { origin, direction ->
        var occ by variable(0.0)
        var sca by variable(1.0)

        val i by variable(0)
        i.for_(0 until 5) {
            val h by 0.01 + 0.15 * (i / 4.0)
            val d by scene(origin + h * direction)
            occ += (h - d) * sca
            sca *= 0.95
        }
        saturate(1.0 - 1.5 * occ)
    }
}


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
                        val coord0 = it + Vector3(
                            cos(it.z * 5.432 + p_time * 2.2),
                            cos(it.x * 10.0 - p_time),
                            sin(it.y * 8.43 + p_time)
                        ) * 0.3

                        val d by sdSphere(coord0, radius)
                        val d2 by sdSphere(it, radius * 50.0)
                        min(d, -d2)
                    }

                    val sceneNormal by gradient(scene, 1E-3)
                    val marcher by march(scene)
                    val aoCalcer by calcAO(scene)
                    val result by marcher(rayOrigin, rayDir)

                    val n by 0.0
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