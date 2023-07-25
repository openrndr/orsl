package minivoxel

import org.openrndr.extra.shadergenerator.dsl.Sampler2D
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.dsl.shadestyle.fragmentTransform
import org.openrndr.extra.shadergenerator.phrases.dsl.filter.shadeStyleFilter1to1
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
fun composeBuffers() = shadeStyleFilter1to1 {

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

        val albedo by tex0[v_texCoord0]
        val material by tex1[v_texCoord0]
        val worldNormal by tex3[v_texCoord0].xyz
        val irradiance by tex4[v_texCoord0].x

        val ambOcc by tex2[v_texCoord0].x * Vector3(0.125).symbol


        val finalColor = pow(albedo.xyz * ( ambOcc + Vector3(1.0).symbol * irradiance ) , Vector3(1.0/2.2).symbol)
        x_fill = Vector4(finalColor, 1.0)


    }
}