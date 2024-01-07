package org.openrndr.orsl.shadergenerator.phrases.dsl.filter


import org.openrndr.draw.ShadeStyle
import org.openrndr.draw.ShadeStyleFilter1to1

fun shadeStyleFilter1to1(f: ShadeStyle.() -> Unit): ShadeStyleFilter1to1 {
    val ss = ShadeStyle()
    ss.apply(f)
    return ShadeStyleFilter1to1(ss)
}
