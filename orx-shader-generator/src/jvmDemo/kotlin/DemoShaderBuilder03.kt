import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.extra.shadergenerator.dsl.Symbol
import org.openrndr.extra.shadergenerator.dsl.structs.getValue
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.dsl.shadestyle.vertexTransform
import org.openrndr.extra.shadergenerator.dsl.structs.setValue
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.gradient
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.jacobian
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

class Material : Struct<Material>() {
    var color by field<Vector4>()
    var roughness by field<Double>()
    var metallic by field<Double>()
}

var Symbol<Material>.color by Material::color
var Symbol<Material>.roughness by Material::roughness
var Symbol<Material>.metallic by Material::metallic


fun main() {
    application {
        program {
            val sphere = sphereMesh()
            val sphereMaterial = Material().apply {
                color = ColorRGBa.PINK.toLinear().toVector4()
                metallic = 0.0
                roughness = 0.0
            }
            extend(Orbital())
            extend {
                drawer.shadeStyle = shadeStyle {
                    vertexTransform {
                        val p_time by parameter<Double>()
                        x_position += simplex34( Vector4(x_position, p_time)) * 0.1
                    }

                    fragmentTransform {
                        val p_material by parameter<Material>()
                        val p_cameraPosition by parameter<Vector3>()

                        val pow5 by function<Double, Double> { x ->
                            val x2 by x * x
                            x2 * x2 * x
                        }

                        val s13 by function<Vector3, Double> { simplex13(it) }
                        val g13 by gradient(s13)

                        g13(p_cameraPosition)


                        val s3 by function<Vector3, Vector3> { simplex33(it) }
                        val g3 by jacobian(s3)
                        g3(p_cameraPosition)

                        val D_GGX by function<Double, Double, Vector3, Double> { linearRoughness, NoH, h ->
                            val oneMinusNoHSquared by 1.0 - NoH * NoH
                            val a by (NoH * linearRoughness)
                            val k by linearRoughness / (oneMinusNoHSquared + a * a)
                            val d by k * k * (1.0 / Math.PI)
                            d
                        }

                        val V_SmithGGXCorrelated by function<Double, Double, Double, Double> { linearRoughness, NoV, NoL ->
                            val a2 by linearRoughness * linearRoughness
                            val GGXV by NoL * sqrt((NoV - a2 * NoV) * NoV + a2)
                            val GGXL by NoV * sqrt((NoL - a2 * NoL) * NoL + a2)
                            0.5 / (GGXV + GGXL)
                        }

                        val F_Schlick by function<Vector3, Double, Vector3> { f0, VoH ->
                            f0 + (Vector3.ONE - f0) * pow5(1.0 - VoH)
                        }

                        val F_Schlick3 by function<Double, Double, Double, Double> { f0, f90, VoH ->
                            f0 + (f90 - f0) * pow5(1.0 - VoH)
                        }

                        val Fd_Burley by function<Double, Double, Double, Double, Double> { linearRoughness, NoV, NoL, LoH ->
                            val f90 by 0.5 + 2.0 * linearRoughness * LoH * LoH
                            val one by 1.0
                            val lightScatter by F_Schlick3(one, f90, NoL)
                            val viewScatter by F_Schlick3(one, f90, NoV)
                            lightScatter * viewScatter * (1.0 / Math.PI)
                        }

                        val d by v_viewNormal.dot(Vector3(0.0, 1.0, 0.0))

                        val baseColor by p_material.color.xyz
                        val roughness by p_material.roughness //* (cos(v_worldPosition.y*100.0)*0.5+0.5)
                        val linearRoughness by roughness * roughness

                        val m = abs(simplex13(v_worldPosition))
                        val metallic by p_material.metallic * m
                        val intensity by 1.0

                        val diffuseColor by (1.0 - metallic) * baseColor
                        val fBase by Vector3(0.04, 0.04, 0.04)
                        val f0 by fBase * (1.0 - metallic) + baseColor * metallic
                        val attenuation by 1.0

                        val v by -(v_worldPosition - p_cameraPosition).normalized
                        val n by v_worldNormal.normalized
                        val l by Vector3(0.6, 0.7, -0.7).normalized
                        val h by (v + l).normalized

                        val NoV by abs(n.dot(v)) + 1e-5
                        val NoL by saturate(n.dot(l))
                        val NoH by saturate(n.dot(h))
                        val LoH by saturate(l.dot(h))

                        val D by D_GGX(linearRoughness, NoH, h)
                        val V by V_SmithGGXCorrelated(linearRoughness, NoV, NoL)
                        val F by F_Schlick(f0, LoH)

                        val Fr by (D * V) * F
                        val Fd by diffuseColor * Fd_Burley(linearRoughness, NoV, NoL, LoH)
                        val color by (Fd + Fr) * (intensity * attenuation * NoL) + diffuseColor * 0.1

                        val gamma by Vector3(1.0 / 2.2, 1.0 / 2.2, 1.0 / 2.2)
                        x_fill = Vector4(pow(color, gamma), 1.0)
                    }

                    //println(fragmentPreamble)
                }
                drawer.shadeStyle?.parameter("material", sphereMaterial)

                val cameraPosition = ((drawer.view).inversed * Vector4(0.0, 0.0, 0.0, 1.0)).xyz
                drawer.shadeStyle?.parameter("cameraPosition", cameraPosition)

                for (j in -2..2) {
                    for (i in -2..2) {
                        drawer.isolated {
                            sphereMaterial.metallic = (j + 2) / 4.0
                            sphereMaterial.roughness = ((i + 2) / 4.0) * 0.95 + 0.025
                            drawer.shadeStyle?.parameter("material", sphereMaterial)
                            drawer.shadeStyle?.parameter("time", seconds)
                            drawer.translate(i * 2.0, j * 2.0, 0.0, target = TransformTarget.MODEL)
                            drawer.vertexBuffer(sphere, DrawPrimitive.TRIANGLES)
                        }
                    }
                }
            }
        }
    }
}