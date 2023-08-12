package minivoxel

import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.PI

fun composeRadiance() = shadeStyleFilter1to1 {
    fragmentTransform {
        val tex0 by parameter<Sampler2D>() // albedo
        val tex1 by parameter<Sampler2D>() // material
        val tex2 by parameter<Sampler2D>() // ambient occlusion
        val tex3 by parameter<Sampler2D>() // normal
        val tex4 by parameter<Sampler2D>() // irradiance
        val v_texCoord0 by parameter<Vector2>()

        val sh by function<Vector3, Vector3> { n ->
            Vector3(0.754554516862612, 0.748542953903366, 0.790921515418539).symbol +
                    Vector3(-0.083856548007422, 0.092533500963210, 0.322764661032516).symbol * (n.y) +
                    Vector3(0.308152705331738, 0.366796330467391, 0.466698181299906).symbol * (n.z) +
                    Vector3(-0.188884931542396, -0.277402551592231, -0.377844212327557).symbol * (n.x)
        }

        val albedo by tex0[v_texCoord0].xyz
        val material by tex1[v_texCoord0]
        val metallic by material.y
        val emissive by material.w
        val worldNormal by tex3[v_texCoord0].xyz
        val irradiance by tex4[v_texCoord0].xy

        val f by 0.04 * (1.0 - metallic)
        val f0 by Vector3(f, f, f) + albedo * metallic
        val F by F_Schlick(f0, 1.0.symbol)
        val diffuse by Vector3(1.0, 1.0, 1.0).symbol * albedo * irradiance.x * (1.0 - metallic)
        val specular by Vector3(1.0, 1.0, 1.0).symbol * irradiance.y * F
        val ambient by tex2[v_texCoord0].x * sh(worldNormal) * (1.0 / PI) * 0.25 * (1.0 - metallic)

        val finalColor = Vector3.ONE * emissive + (diffuse + specular + ambient) * (1.0 - emissive)
        x_fill = Vector4(finalColor, 1.0)
    }
}


fun composeFinal() = shadeStyleFilter1to1 {
    fragmentTransform {
        val tex0 by parameter<Sampler2D>() // radiance
        val tex1 by parameter<Sampler2D>() // indirect specular (ssr)
        val tex2 by parameter<Sampler2D>() // world normal
        val tex3 by parameter<Sampler2D>() // world position
        val tex4 by parameter<Sampler2D>() // material
        val tex5 by parameter<Sampler2D>() // albedo
        val tex6 by parameter<Sampler2D>() // ambient occlussion
        val v_texCoord0 by parameter<Vector2>()
        val p_cameraPosition by parameter<Vector3>()

        val radiance by tex0[v_texCoord0].xyz
        val indirect by tex1[v_texCoord0].xyz
        val worldNormal by tex2[v_texCoord0].xyz.normalized
        val worldPosition by tex3[v_texCoord0].xyz
        val material by tex4[v_texCoord0]
        val albedo by tex5[v_texCoord0].xyz
        val ambOcc by tex6[v_texCoord0].x

        val v by (worldPosition - p_cameraPosition).normalized
        val nOv by worldNormal.dot(v)

        val metallic by material.y
        val roughness by material.z

        val prefilteredDFG_Karis by function<Double, Double, Vector2> { roughness, nOv ->
            val c0 by org.openrndr.math.Vector4(-1.0, -0.0275, -0.572, 0.0)
            val c1 by org.openrndr.math.Vector4(1.0, 0.0425, 1.040, -0.040)

            val r by c0 * roughness + c1
            val a004 by min(r.x * r.x, exp2(-9.28 * nOv)) * r.x + r.y;
            Vector2(-1.04, 1.04) * a004 + r.zw
        }

        val f0 = Vector3(0.04, 0.04, 0.04) * (1.0 - metallic) + albedo * metallic
        val dfg by prefilteredDFG_Karis(roughness, nOv)
        val specularColor by f0 * dfg.x + Vector3(dfg.y, dfg.y, dfg.y)

        val color by radiance + specularColor * indirect// * ambOcc

        val aces by function<Vector3, Vector3> { x ->
            val a by 2.51;
            val b by 0.03;
            val c by 2.43;
            val d by 0.59;
            val e by 0.14;
            (x * (a * x + b)) / (x * (c * x + d) + e);
        }
        val acesColor by aces(color)
        val finalColor = pow(acesColor, Vector3(1.0 / 2.2).symbol)
        x_fill = Vector4(finalColor, 1.0)
    }
}
