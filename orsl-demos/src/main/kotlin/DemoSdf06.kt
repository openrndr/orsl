import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.orsl.shadergenerator.dsl.*
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.orsl.shadergenerator.dsl.structs.setValue
import org.openrndr.orsl.shadergenerator.phrases.sdf.*
import org.openrndr.math.*
import org.openrndr.math.transforms.normalMatrix
import org.openrndr.orsl.extension.gradient.functions.gradient
import org.openrndr.orsl.extension.sdf.phrases.sdSphere
import org.openrndr.orsl.extension.sdf.phrases.sphericalDistribution


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

                    val shadowScene by function<Vector3, Double> {
                        val c by 1000
                        val d by sphericalDistribution(it, c)
                        val radius by 1.0
                        val ds by sdSphere(it, radius)

                        val mds by sdSphere(it - d.xyz, radius * 0.05)
                        val d2 by sphericalDistribution(it - d.xyz, c)

                        val nds by sdSphere(it - d.xyz - d2.xyz * 0.05, radius * 0.05*0.05)

                        val xd by min(mds, nds)
                        val db by min(xd, ds)
                        //val db by ds + (d.w-0.3)

                        val mr by 50.0
                        min(-sdSphere(it, mr),db)
                    }


                    val marcher by march(shadowScene, tolerance = 1E-4, stepScale = 0.9)
                    val result by marcher(rayOrigin, rayDir)

                    val normal by run {
                        val sceneNormal by gradient(shadowScene, 1E-4)
                        sceneNormal(result.position).normalized
                    }

                    val light by Vector3(0.6, 0.6, 0.6).normalized
//                    val occ by run {
//                        val shadows by calcShadow(shadowScene, iterations = 32)
//                        val tmin by 0.01
//                        val tmax by 40.0
//                        shadows(result.position, light, tmin, tmax)
//                    }
                    val occ by 1.0

                    val hlf by (light - rayDir).normalized
                    val diffuse by saturate(normal.dot(hlf)) * occ

                    val d16 by 16.0
                    val d5 by 5.0

                    val specular by pow( saturate( normal.dot(hlf)), 1.0 + d16)*
                            diffuse *
                            (0.04 + 0.96*pow( saturate(1.0+hlf.dot(rayDir)), d5 ))

                    val amb by run {
                        val aoCalcer by calcAO(shadowScene, intensity = 0.5, distance = 1.0, iterations = 32, falloff = 0.85)
                        aoCalcer(result.position, normal)
                    }

                    val w by Vector3(0.5, 0.5,0.5)
                    val lw by Vector3(0.5, 0.5, 0.5)
                    val finalColor by Vector3(1.0, 1.0, 1.0) * 0.35 * diffuse + w * amb + lw * 6.0 * specular

                    val n by 10
                    val sp = sphericalDistribution(result.position, n).w

                    x_fill = Vector4(finalColor *  (1.0/ (1.0 + sp)), 1.0)


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