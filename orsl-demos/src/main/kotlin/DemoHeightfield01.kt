import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.functions.Functions
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.orsl.shadergenerator.phrases.sdf.MarchResult
import org.openrndr.orsl.shadergenerator.phrases.sdf.hit
import org.openrndr.orsl.shadergenerator.phrases.sdf.position
import org.openrndr.orsl.shadergenerator.phrases.sdf.travel
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.noise.phrases.value12D


fun ShaderBuilder.raycast(
    scene: (x: Symbol<Vector2>) -> FunctionSymbol1<Vector2, Vector3>,
    tmin: Double,
    tmax: Double
): Functions.Function2PropertyProvider<Vector3, Vector3, MarchResult> {
    return Functions.Function2PropertyProvider(
        true, this@raycast, staticType<Vector3>(),
        staticType<Vector3>(),

        staticType<MarchResult>()
    ) { origin, direction ->
        val result by MarchResult()
        val False by false
        result.hit = False
        emit(
            """float t = $tmin;
                
for (int i_ = 0; i_ < 100; ++i_) {     
    vec3 position = x__ + t * y__;
    vec3 pn =  ${scene(symbol("position.xz")).name};
    float h = position.y - pn.x;
    result.position = position;
    result.travel = t;
    if( abs(h)<(0.0015*t) || t>$tmax ) {
        result.hit = t < $tmax;
        vec2 n = pn.yz;
        n = normalize(n);
        result.normal = normalize(vec3(n.x, 1.0, n.y));
        break;  
    }
    t += 0.4*h;
    
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
                    val p_origin by parameter<Vector3>()
                    val p_time by parameter<Double>()
                    val va_texCoord0 by parameter<Vector2>()
                    val p_viewMatrix by parameter<Matrix44>()
                    val fixedCoord = Vector2(va_texCoord0.x, 1.0 - va_texCoord0.y)

                    val rayDir by (p_viewMatrix * Vector4(fixedCoord - Vector2(0.5), -1.0, 0.0)).xyz.normalized
                    val rayOrigin by p_origin


                    val scene by function<Vector2, Vector3> {
                        val h by value12D(it* 0.001)*1000.0 + value12D(it * 0.002) * 500.0 +  value12D(it * 0.004) * 250.0       +  value12D(it * 0.008) * 125.0
                        h  * 0.5
                    }

                    val sceneH by function<Vector2, Vector3> {
                        val h by value12D(it* 0.001)*1000.0 +
                                value12D(it * 0.002) * 500.0 +
                                value12D(it * 0.004) * 250.0       +
                                value12D(it * 0.008) * 125.0 +
                                value12D(it * 0.016) * 62.5 +
                                value12D(it * 0.032) * 31.25 +
                                value12D(it * 0.064) * 15.625
                        h  * 0.5
                    }


                    val normal by function<Vector3, Double, Vector3> {
                        pos, t ->
                        val z by 0.0
                        val eps by Vector2(0.001 *t, z)
                        val dx by sceneH(pos.xz - eps).x - scene(pos.xz + eps).x
                        val dy by 2.0 * eps.x
                        val dz by sceneH(pos.xz - eps.yx).x - scene(pos.xz + eps.yx).x

                        val n by Vector3(dx, dy, dz).normalized
                        n
                    }


                    val raycaster by raycast(scene, 1.0, 50000.0)
                    val result by raycaster(rayOrigin, rayDir)

                    val white by Vector3(1.0, 1.0, 1.0)
                    val black by Vector3(0.0, 0.0, 0.0)
                    val finalColor by black.elseIf(result.hit) { white *
                        smoothstep(-0.1, 0.1,normal(result.position, result.travel).y) * (1.0/ (1.0+result.travel*0.0001))

                    }

                    //val normal by sceneNormal(result.position).normalized
                    //val shade = normal.dot(Vector3(0.0, 0.0, 1.0))
                    //val finalColor by g_color * shade

                    x_fill = Vector4(finalColor, 1.0)
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