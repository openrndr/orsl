package org.openrndr.orsl.shadergenerator.dsl

fun hash(vararg items: Any): UInt {
    var hash = 0
    for (item in items) {
        hash = hash * 31 + item.hashCode()
    }
    return hash.toUInt()
}