package org.openrndr.extra.shadergenerator.phrases.dsl

class Range(val startP: Symbol<Int>? = null, val startV: Int? = null, val endP: Symbol<Int>? = null, val endV: Int? = null)

class BoxRange2(val xrange: Range, val yrange: Range)

operator fun Range.times(other: Range): BoxRange2 {
    return BoxRange2(this, other)
}