//import org.openrndr.application
//import org.openrndr.draw.shadeStyle
//import org.openrndr.extra.shadergenerator.phrases.MyShaderPhrases
//import org.openrndr.extra.shadergenerator.phrases.MyShaderPhrasesIndex
//import org.openrndr.extra.shadergenerator.phrases.PhraseResolver
//import org.openrndr.extra.shadergenerator.phrases.dsl.*
//import org.openrndr.extra.shadergenerator.phrases.phrases.*
//import org.openrndr.math.Vector2
//import org.openrndr.math.Vector3
//import org.openrndr.math.Vector4
//
//fun main() {
//    application {
//        program {
//            val resolver = PhraseResolver()
//            resolver.indices.add(ArrayPhrasesIndex(ArrayPhrases()))
//            resolver.indices.add(HashPhrasesIndex(HashPhrases()))
//            resolver.indices.add(FastMathPhrasesIndex(FastMathPhrases()))
//            resolver.indices.add(Mod289PhrasesIndex(Mod289Phrases()))
//            resolver.indices.add(PermutePhrasesIndex(PermutePhrases()))
//            resolver.indices.add(SimplexPhrasesIndex(SimplexPhrases()))
//            resolver.indices.add(ConstPhrasesIndex(ConstPhrases()))
//            resolver.indices.add(EasingPhrasesIndex(EasingPhrases()))
//
//
//            val sb = FragmentTransformBuilder()
//            sb.apply {
//
//                val p_time by parameter<Double>()
//                val d by (c_boundsPosition.xy - Vector2(0.5, 0.5)).length
//                val a by array<Vector2>(5)
//                a[0] = c_boundsPosition.xy
//                val f by function<Vector2, Vector2> { it * 2.0 }
//                val q by a.map(f).map(f).map(f)
//                val q2 by q.drop(1)
//                val n by hash42(c_boundsPosition.xy * 100.0)
//                val n3 by simplex12(c_boundsPosition.xy)
//                val n4 by simplex13(Vector3(c_boundsPosition.xy, p_time))
//                val sd by smoothstep(1.0, 0.9, d * 2.0) * smoothstep(0.3, 0.9, d * 2.0)
//                x_fill = Vector4(n4 + sd + n.x * 0.4, sd * 0.5 + n.y * 0.4, sd * 0.7, 1.0)
//
//            }
//            println(sb.code)
//            extend {
//                drawer.shadeStyle = shadeStyle {
//                    fragmentPreamble = resolver.preprocessShader(sb.preamble)
//                    fragmentTransform = sb.code
//                    parameter("time", seconds)
//                }
//                drawer.circle(drawer.bounds.center, 200.0)
//            }
//        }
//    }
//}