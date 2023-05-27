import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.shadergenerator.dsl.*
import org.openrndr.extra.shadergenerator.dsl.functions.Functions
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

class MarchResult : Struct<MarchResult>() {
    var position by field<Vector3>()
    var hit by field<Boolean>()
}

var Symbol<MarchResult>.position by MarchResult::position
var Symbol<MarchResult>.hit by MarchResult::hit

fun ShaderBuilder.march(
    scene: (x: Symbol<Vector3>) -> FunctionSymbol1<Vector3, Double>): Functions.Function2PropertyProvider<Vector3, Vector3, MarchResult> {
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
    position_ += distance_ * direction_;
}"""
        )
        result
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
                    val p_time by parameter<Double>()
                    val va_texCoord0 by parameter<Vector2>()

                    val rayDir by Vector3(va_texCoord0 - Vector2(0.5), 1.0).normalized
                    val rayOrigin by Vector3(0.0, 0.0, -3.0)

                    val sdSphere by function<Vector3, Double, Double> { p, s ->
                        p.length - s
                    }

                    val scene by function<Vector3, Double> {
                        val radius by 1.0
                        //val d by sdSphere(it + simplex34(Vector4(it*3.0, p_time))*0.1, radius)
                        val d by sdSphere(it + Vector3(cos(it.z*5.432), cos(it.x*10.0-p_time), sin(it.y*3.43+p_time))*0.1, radius)
                        d
                    }

                    val sceneNormal by gradient(scene, 1E-3)

                    val marcher by march(scene)
                    val result by marcher(rayOrigin, rayDir)
                    val normal by sceneNormal(result.position).normalized
                    val c by normal.dot(Vector3(0.0, 0.0, -1.0))
                    x_fill = Vector4(c, c, c, 1.0)
                }
                parameter("time", seconds)
            }
            extend {
                ss.parameter("time", seconds)
                drawer.shadeStyle = ss

                drawer.rectangle(drawer.bounds)
            }
        }
    }
}