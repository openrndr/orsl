package org.openrndr.extra.shadergenerator.dsl

sealed interface Image

interface Image2D : Image

interface IntImage2D : Image2D

interface IntRImage2D : Image2D

interface UIntImage2D : Image2D

interface Image3D : Image

interface IntRImage3D : Image3D

interface ImageCube : Image

interface Image2DArray : Image

interface ImageCubeArray : Image

interface ImageBuffer : Image

