import org.openrndr.application
import org.openrndr.draw.*
import org.openrndr.extra.shadergenerator.compute.computeStyle
import org.openrndr.extra.shadergenerator.compute.computeTransform
import org.openrndr.extra.shadergenerator.compute.execute
import org.openrndr.extra.shadergenerator.dsl.Image2D
import org.openrndr.extra.shadergenerator.dsl.IntRImage2D
import org.openrndr.extra.shadergenerator.dsl.functions.function
import org.openrndr.extra.shadergenerator.dsl.functions.symbol
import org.openrndr.extra.shadergenerator.phrases.dsl.compute.return_
import org.openrndr.extra.shadergenerator.phrases.dsl.functions.extra.*
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.cos


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
                    val p_time by parameter<Double>()
                    val p_dofFocalDist by parameter<Double>()
                    val p_dofAmount by parameter<Double>()
                    val p_screen by parameter<Image2D>()
                    val p_atomic by parameter<IntRImage2D>()

                    val projParticle by function<Vector3, Vector3> {
                        var p by variable(it)
                        val ogz by p.z
                        p += Vector3(0.0, 0.0, 4.0).symbol
                        p /= p.z * 0.4
                        Vector3(p.x, p.y, ogz)
                    }

                    setSeed(c_giid)

                    val iters = 5
                    var p by variable(uniform3() * 2.0 - Vector3.ONE.symbol)
                    val dir by (uniform3() * 2.0 - Vector3.ONE.symbol).normalized

                    val R by p_screen.size().double
                    val focusDist by (p_dofFocalDist * 2.0 - 1.0) * 2.0
                    val dofFac by (1.0 / Vector2(R.x / R.y, 1.0.symbol)) * p_dofAmount
                    val i by variable<Int>()

                    var fd by variable<Double>(0.0)
                    i.for_(0 until iters) {
                        val pp by p //erot(p, Vector3(1.0,1.0,1.0).normalized.symbol, (p.x+p.y+p.z+p_time) * 3.0*p_d)
                        val dist0 by sdSphere(pp, 3.0.symbol) + value13(p  + Vector3(p_time, p_time, p_time))*4.0
                        val dist by dist0
                        p = p + dir * dist * 0.8
                        fd = dist
                    }
                    i.for_(0 until 4096) {
                        val q by projParticle(p + Vector3(0.0.symbol, 0.0.symbol, (uniform1()-0.5)*1.0))
                        val k by q.xy * Vector2(9.0 / 16.0, 1.0) + uniformDisk() * abs(q.z - focusDist) * 0.5 * dofFac
                        val uv by k / 2.0 + Vector2(0.5)
                        val cc by (uv * R).int
                        doIf(
                            (abs(fd) lt 0.01) and
                                    (cc.x gte 0) and (cc.y gte 0) and (cc.x lt R.x.int) and (cc.y lt R.y.int)
                        ) {
                            val b by p_atomic.atomicAdd(cc, 1)
                        }
                    }
                }
            }

            splat.parameter("dofAmount", 0.116)
            splat.parameter("dofFocalDist", 0.15)
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
                    col = log(col) *0.15
                    col = pow(col, Vector3( 2.2).symbol)
                    col *= Vector3(1.0, 1.2, 1.3)
                    p_screen.store(c_giid.xy.int, Vector4(col, 1.0))
                    p_atomic.store(c_giid.xy.int, 0.symbol)
                }
            }

            mainImage.parameter("atomic", atomic.imageBinding(0, ImageAccess.READ_WRITE))
            mainImage.parameter("screen", cb.imageBinding(0, ImageAccess.WRITE))

//            extend(ScreenRecorder()) {
//                maximumDuration = 20.0
//            }
            extend {
                splat.parameter("dofFocalDist", cos(seconds) * 0.5 + 0.5)
                splat.parameter("time", seconds)
                splat.execute(32,32)
                mainImage.execute(width / mainImage.workGroupSize.x, height / mainImage.workGroupSize.y, 1)
                drawer.image(cb)
            }

        }
    }
}