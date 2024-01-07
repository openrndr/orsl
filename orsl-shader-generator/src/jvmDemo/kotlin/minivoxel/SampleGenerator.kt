package minivoxel

import org.openrndr.math.Polar
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

fun hemisphereSamples(samples: Int) : List<Vector3> {
    val phi = (1 + sqrt(5.0)) / 2.0
    return (0 until samples).map {
        val theta = phi * it
        val r = sqrt(it + 0.5) / sqrt(samples.toDouble())
        val p = Polar(theta, r).cartesian
        val z = sqrt(1.0 - p.squaredLength)
        p.vector3(z = z)
    }
}

fun spiralSamples(samples: Int, rings: Int) : List<Vector2> {
    val angleStep = 360.0 * rings.toDouble() / samples
    val radiusStep = 1.0 / samples

    return (0 until samples).map {
        val angle = angleStep * it
        val radius = radiusStep * (it + 1)
        Polar(angle, radius.pow(0.75)).cartesian
    }
}