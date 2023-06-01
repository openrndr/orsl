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
): Functions.Function2PropertyProvider<Vector3, Vector3, MarchResult> {
    return Functions.Function2PropertyProvider(
        this@march, staticType<Vector3>(),
        staticType<Vector3>(),

        staticType<MarchResult>()
    ) { position, direction ->
        val result by MarchResult()
        val False by false
        result.hit = False
        emit(
            """float sum_ = 0.0;
vec3 position_ = x__;
vec3 direction_ = y__;
for (int i_ = 0; i_ < 200; ++i_) {     
    float distance_ = ${scene(symbol("position_")).name};
    
    if (abs(distance_) < 1E-2) {
        result.hit = true;
        result.position = position_;
        break;
    }
    position_ += distance_ * direction_ * 0.5;
}"""
        )
        result
    }
}



fun ShaderBuilder.calcAO(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>
): Functions.Function2PropertyProvider<Vector3, Vector3, Double> {
    return Functions.Function2PropertyProvider(
        this@calcAO, staticType<Vector3>(),
        staticType<Vector3>(),

        staticType<Double>()
    ) { position, direction ->
        emit(
            """float occ_ = 0.0;
float sca_ = 1.0;
vec3 position_ = x__;
vec3 direction_ = y__;
for (int i_ = 0; i_ < 5; ++i_) {     
    float h_ = 0.01 + 0.15 * float(i_)/4.0;
    float d_ = ${scene(symbol("position_ + h_ * direction_")).name};
    occ_ += (h_ - d_) * sca_;
    sca_ *= 0.95;    
}

float r = clamp(1.0 - 1.5 * occ_, 0.0, 1.0);
"""
        )

        symbol("r")
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