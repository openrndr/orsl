package org.openrndr.orsl.shadergenerator.compute

import org.openrndr.draw.ComputeStyle
import org.openrndr.orsl.shadergenerator.annotations.ShaderBookIndex
import org.openrndr.orsl.shadergenerator.dsl.UIntVector3
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.ShadeStyleBuilder
import org.openrndr.orsl.shadergenerator.dsl.shadestyle.preprocessor
import org.openrndr.orsl.shadergenerator.dsl.functions.Image2DFunctions
import org.openrndr.orsl.shadergenerator.dsl.functions.IntRImage2DFunctions
import org.openrndr.orsl.shadergenerator.dsl.symbol

class ComputeTransformBuilder() : ShadeStyleBuilder(),

Image2DFunctions, IntRImage2DFunctions {

    /**
     * Local invocation id
     *
     * This is the current invocation of the shader within the work group. Each of the XYZ components will be on the half-open range [0, gl_WorkGroupSize.XYZ).
     */
    val c_liid = symbol<UIntVector3>("gl_LocalInvocationID")

    /**
     * Global invocation id
     *
     * This value uniquely identifies this particular invocation of the compute shader among all invocations of this compute dispatch call. It's a short-hand for the math computation: gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
     */
    val c_giid = symbol<UIntVector3>("gl_GlobalInvocationID")


    /**
     * Work group size
     */
    val c_wgs = symbol<UIntVector3>("gl_WorkGroupSize")

    /** Work group id
     *
     * This is the current work group for this shader invocation. Each of the XYZ components will be on the half-open range [0, gl_NumWorkGroups.XYZ).
     */
    val c_wgid = symbol<UIntVector3>("gl_WorkGroupID")


    /**
     * Local invocation index
     *
     * This is a 1D version of gl_LocalInvocationID. It identifies this invocation's index within the work group. It is short-hand for this math computation:
     * gl_LocalInvocationIndex =
     *           gl_LocalInvocationID.z * gl_WorkGroupSize.x * gl_WorkGroupSize.y +
     *           gl_LocalInvocationID.y * gl_WorkGroupSize.x +
     *           gl_LocalInvocationID.x;
     */
    val c_lii = symbol<UInt>("gl_LocalInvocationIndex")
}

fun ComputeStyle.computeTransform(vararg indices: ShaderBookIndex<*>, builder: ComputeTransformBuilder.() ->Unit) {
    val ctb = ComputeTransformBuilder()
    ctb.push()
    ctb.builder()
    val prep = preprocessor(*indices)
    computePreamble = prep.preprocessShader(ctb.preamble)
    computeTransform = prep.preprocessShader(ctb.code)
    ctb.pop()
}