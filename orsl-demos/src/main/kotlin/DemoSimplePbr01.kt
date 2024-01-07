import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.camera.Orbital
import org.openrndr.extra.meshgenerators.sphereMesh
import org.openrndr.orsl.shadergenerator.dsl.Symbol
import org.openrndr.orsl.shadergenerator.dsl.functions.function
import org.openrndr.orsl.shadergenerator.dsl.structs.getValue
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.orsl.shadergenerator.dsl.structs.setValue
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4
import org.openrndr.orsl.extension.pbr.functions.D_GGX
import org.openrndr.orsl.extension.pbr.functions.F_Schlick
import org.openrndr.orsl.extension.pbr.functions.Fd_Burley
import org.openrndr.orsl.extension.pbr.functions.V_SmithGGXCorrelated

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
                    fragmentTransform {
                        val p_material by parameter<Material>()
                        val p_cameraPosition by parameter<Vector3>()



                        val d by v_viewNormal.dot(Vector3(0.0, 1.0, 0.0))

                        val baseColor by p_material.color.xyz
                        val roughness by p_material.roughness
                        val linearRoughness by roughness * roughness

                        val metallic by p_material.metallic
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

                        val D by D_GGX(linearRoughness, NoH)
                        val V by V_SmithGGXCorrelated(linearRoughness, NoV, NoL)
                        val F by F_Schlick(f0, LoH)

                        val Fr by (D * V) * F
                        val Fd by diffuseColor * Fd_Burley(linearRoughness, NoV, NoL, LoH)
                        val color by (Fd + Fr) * (intensity * attenuation * NoL) + diffuseColor * 0.1

                        val gamma by Vector3(1.0 / 2.2, 1.0 / 2.2, 1.0 / 2.2)
                        x_fill = Vector4(pow(color, gamma), 1.0)

                    }
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