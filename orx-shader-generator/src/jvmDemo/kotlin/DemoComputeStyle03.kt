import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.dsl.Image2D
import org.openrndr.extra.shadergenerator.dsl.IntRImage2D
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.cos

// based on compute.toys entry by wrighter
// https://compute.toys/view/68

fun main() {
    application {
        configure {
            width = 1280
            height = 720
        }
        program {
            val atomic = colorBuffer(width, height, type = ColorType.SINT32_INT, format = ColorFormat.R)
            val cb = colorBuffer(width, height)
            val splat = computeStyle {
                this.workGroupSize = IntVector3(256, 1, 1)
                computeTransform {
                    var seed by global<UInt>()
                    val p_mouse by parameter<Vector2>()
                    val p_time by parameter<Double>()
                    val p_dofFocalDist by parameter<Double>()
                    val p_dofAmount by parameter<Double>()
                    val p_a by parameter<Double>()
                    val p_b by parameter<Double>()
                    val p_c by parameter<Double>()
                    val p_screen by parameter<Image2D>()
                    val p_atomic by parameter<IntRImage2D>()

                    val hash_u by function<UInt, UInt> {
                        var a by variable(it)
                        a = a xor (a shr 16)
                        a = a * 0x7feb352du
                        a = a xor (a shr 15)
                        a = a * 0x846ca68bu
                        a = a xor (a shr 16)
                        a
                    }

                    val hash_f by function<Double> {
                        val s = hash_u(seed)
                        seed = s
                        s.double / 0xffffffffU.symbol.double
                    }

                    val hash_v2 by function<Vector2> {
                        Vector2(hash_f(), hash_f())
                    }

                    val hash_v3 by function<Vector3> {
                        Vector3(hash_f(), hash_f(), hash_f())
                    }

                    val sample_disk by function<Vector2> {
                        val r by hash_v2()
                        Vector2(sin(r.x * Math.PI * 2.0), cos(r.x * Math.PI * 2.0)) * sqrt(r.y)
                    }

                    val coc by function<Double, Double, Double, Double, Double> { a, f, zf, z ->
                        (a * f * (zf - z)) / (zf * (z - f))
                    }

                    val projParticle by function<Vector3, Vector3> {
                        var p by variable(it)
                        p = erot(p, Vector3.UNIT_Y.symbol, p_mouse.x * 0.2)
                        p = erot(p, Vector3.UNIT_X.symbol, p_mouse.y * 0.2)
                        //p += Vector3(0.0, 0.0, 4.0).symbol
                        //p /= p.z * 0.4
                        //p = Vector3(p.x, p.y, it.z)
                        p
                    }

                    seed = hash_u(c_giid.x + hash_u(c_giid.y * 200U) * 20U + hash_u(c_giid.x) * 250U)
                    seed = hash_u(seed)

                    val iters = 20
                    val t by p_time - hash_f() * (1.0 / 30.0)
                    val env by t + sin(t) * 0.5
                    val envb by sin(t * 0.45)
                    var p by variable(hash_v3())

                    val R by p_screen.size().double
                    val focusDist by (p_dofFocalDist * 2.0 - 1.0) * 2.0
                    val dofFac by (1.0 / Vector2(R.x / R.y, 1.0.symbol)) * p_dofAmount
                    val i by variable<Int>()
                    i.for_(0 until iters) {
                        val r by hash_f()
                        val k by if_(r lt 0.3) {
                            p = p + (Vector3(1.0).symbol.mix(Vector3(4.0).symbol, p_a)) + Vector3(envb, envb, envb)
                            p = erot(p, Vector3(1.0, 0.0, 0.0).symbol, env * 0.2)
                            0.0.symbol
                        }.elseIf(r lt 0.3.symbol.mix(0.7.symbol, p_b)) {
                            p = erot(p, Vector3.UNIT_X.symbol, env * 0.2)
                            p += Vector3(-1.0, 0.4, 0.0)
                            p /= p.dot(p)
                            p = p * Vector3(2.0, 1.5, 1.2).symbol * 1.5
                            0.0.symbol
                        } else_ {
                            p -= Vector3(-0.2 * p_c * 5.0, 0.2.symbol, 0.2.symbol)
                            p /= clamp(p.dot(p), (-4.5).symbol, 10.0.symbol)
                            p = p * Vector3(1.0, 1.5, 1.2) * 3.1;
                            0.0.symbol
                        }
                    }
                    val q by projParticle(p)
                    val k by q.xy + sample_disk() * coc(10.0.symbol, 0.01.symbol, 2.0.symbol, q.z)

                    val uv by k / 2.0 + Vector2(0.5)
                    val cc by (uv * R).int

                    imageMemoryBarrier {
                        doIf(
                            (q.z gt 0.0) and
                            (cc.x gte 0) and (cc.y gte 0) and (cc.x lt R.x.int) and (cc.y lt R.y.int)
                        ) {
                            val b by p_atomic.atomicAdd(cc, 1)
                        }
                    }
                }
            }

            splat.parameter("dofAmount", 0.116)
            splat.parameter("dofFocalDist", 0.15)
            splat.parameter("mouse", mouse.position)
            splat.parameter("a", 0.2)
            splat.parameter("b", 0.4)
            splat.parameter("c", 0.9)
            splat.parameter("time", seconds)
            splat.parameter("screen", cb.imageBinding(0, ImageAccess.READ))
            splat.parameter("atomic", atomic.imageBinding(0, ImageAccess.READ_WRITE))

            val mainImage = computeStyle {
                workGroupSize = IntVector3(16, 16, 1)
                computeTransform {
                    val p_screen by parameter<Image2D>()
                    val p_atomic by parameter<IntRImage2D>()
                    val res by p_screen.size().uint
                    doIf((c_giid.x gt res.x) or (c_giid.y gt res.y)) {
                        return_()
                    }
                    val c by p_atomic.load(c_giid.xy.int).double + 1.0
                    var col by variable(Vector3(c, c, c))
                    val sc by 1244520.7
                    col = log(col) / log(sc)
                    col = pow(col, Vector3(1.0 / 2.2).symbol)
                    col *= Vector3(1.0, 1.2, 1.3)
                    p_screen.store(c_giid.xy.int, Vector4(col, 1.0))
                    p_atomic.store(c_giid.xy.int, 0.symbol)
                }
            }

            mainImage.parameter("atomic", atomic.imageBinding(0, ImageAccess.READ_WRITE))
            mainImage.parameter("screen", cb.imageBinding(0, ImageAccess.WRITE))

            extend {
                splat.parameter("dofFocalDist", cos(seconds) * 0.5 + 0.5)
                splat.parameter("mouse", (mouse.position / window.size) * Math.PI * 2.0)
                splat.parameter("time", seconds)
                splat.parameter("a", cos(seconds * 0.342) * 0.5 + 0.5)
                splat.parameter("b", cos(seconds * 0.5374) * 0.5 + 0.5)
                splat.parameter("c", cos(seconds * 0.4193) * 0.5 + 0.5)
                splat.execute(256, 256, 1)
                mainImage.execute(width / mainImage.workGroupSize.x, height / mainImage.workGroupSize.y, 1)
                drawer.image(cb)
            }

        }
    }
}