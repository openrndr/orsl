# orsl-shader-generator

Tools for authoring shaders using Kotlin's typesafe builders.

## A new shader language: ORSL

The OPENRNDR shader language is a Kotlin specific language. 

## `ShadeStyle` extensions

[Shade style documentation](https://guide.openrndr.org/advancedDrawing/shadeStyles.html) 

A simple OPENRNDR program that uses a shade style looks like this:

```kotlin
fun main() = application {
    program {
        extend(Camera2D())
        extend {
            drawer.shadeStyle = shadeStyle {
                fragmentTransform = """float d = cos(va_texCoord0.x) * 0.5 + 0.5; 
                x_fill = vec4(vec3(d), 1.0);"""
            }
            drawer.circle(drawer.bounds.center, 200.0)
        }
    }
}
```
That is, a short piece of GLSL code is assigned to `fragmentTransform`. Using text for shader code has some downsides: no syntax checks at compile time, no completion in the 
editor, and hidden variables.

`orx-shader-generator` provides a range of extensions to `ShadeStyle` for authoring shade styles using a type-safe builder, or DSL. A shade style written using said extensions looks like this: 

```kotlin
fun main() = application {
    program {
        extend(Camera2D())
        extend {
            drawer.shadeStyle = shadeStyle {
                fragmentTransform {
                    val p_time by parameter<Double>()
                    val va_texCoord0 by varyingIn<Vector2>()
                    val d by value13D(Vector3(va_texCoord0, p_time)*10.0).yzw * 0.5 + Vector3(0.5)
                    x_fill = Vector4(d, 1.0)
                }
                parameter("time", seconds * 0.1)
            }
            drawer.circle(drawer.bounds.center, 200.0)
        }
    }
}
```

## Functions

The shader language supports the creation of local functions (in contrast to GLSL), however it comes with the limitation of
having no support for closures. However, values that are passed in using shader uniforms or vertex attributes can be accessed from within the function scope.

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by varyingIn<Vector2>()
    val f by function<Double, Double> { x ->
        x * x + cos(p_time)
    } 
    val d by f(va_texCoord0.x)
    x_fill = Vector4(d, d, d, 1.0)
}
```

Note that ORSL currently allows one to construct a function that accesses its parent scope. This will, however,result in a shader that cannot be compiled, since the underlying shader language (GLSL) does not support closures. To demonstrate a shader that incorrectly accesses values outside of its local scope: 

```kotlin
val va_texCoord0 by varyingIng<Vector2>()
val notInScope by 2.0
val f by function<Double, Double> { x ->
    x * x + cos(p_time) + notInScope
} 
```

### Mixing Kotlin and ORSL functions

One can use ordinary Kotlin functions as a sort of macro processor.

```kotlin
fragmentTransform {
    fun makeFunction(expensive:Boolean) = function<Double, Double> { x ->
        var y by Variable(0.0)
        // this is a statically evaluated conditional
        if (expensive) {
            y = sqrt(x)
        }
        x * x + cos(y)
    }
    val fExpensive by makeFunction(true)
    val fCheap by makeFunction(false)
    val d by fExpensive(va_texCoord0.x)
    x_fill = Vector4(d, d, d, 1.0)
}
```


## Globals 

There's a work-around for the lacking closure support in the form of global variables.

```kotlin
var mutableGlobal by global<Double>()
var mutableGlobalWithInitialValue by global(5.0)
val immutableGlobal by global(6.0)
```

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by varyingIn<Vector2>()
    var someGlobal by global<Double>()
    val f by function<Double, Double> { x ->
        someGlobal = x
        x * x + cos(p_time) + sin(someGlobal)
    } 
    val d by f(va_texCoord0.x)
    x_fill = Vector4(d, d, d, 1.0)
}
```

## Run {}

A common Kotlin pattern is `run {}` which is used to create anonymous blocks of code with its own local scope. `orx-shader-generator` supports `run {}` blocks.

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by parameter<Vector2>()
     
    val d by run {
        val t by va_texCoord0.x + va_texCoord0.y
        t + 3.0
    }
    x_fill = Vector4(d, d, d, 1.0)
}
```

## For loops

Note that there are two types of for loops that can be used:
 * statically evaluated for loops
 * dynamically evaluated for loops

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by varyingIn<Vector2>()
    
    var sum by variable(0.0)
    // create a dynamic for loop
    var i by variable(0)
    i.for_(0 until 20) {
        sum += i
    }
    val d = cos(sum) * 0.5 + 0.5
    
    x_fill = Vector4(d, d, d, 1.0)
}
```

### Loop nesting

```kotlin
var j by variable(0)
j.for_(0 until 20) {
    var i by variable(0)
    i.for_(0 until 20) {
        sum += i * j    
    }
}
```

### Conditionals

### if expressions

If _expressions_ result in a value. 

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
 
    x_fill = if_(p_time lt 2.0) {
        Vector4(1.0, 0.0, 0.0, 1.0).symbol
    }.elseIf(p_time lt 4.0) {
        Vector4(0.5, 0.5, 0.5, 1.0).symbol
    } else_ {
        Vector4(0.9, 0.4, 0.7, 1.0).symbol
    }
}
```

| Kotlin operator | ORSL operator | 
|-----------------|---------------|
| `<`             | `lt`          |
| `<=`            | `lte`         |
| `==`            | `eq`          |
| `!=`            | `neq`         |
| `>=`            | `gte`         |
|  `>`            | `gt`          |

### doIf statements

`doIf` statements only result in side effects.
```kotlin
fragmentTransform {
val p_time by parameter<Double>()
    x_fill = vec4(1.0, 1.0, 1.0, 1.0)
    doIf(p_time lt 2.0) {
        x_fill = Vector4(1.0, 0.0, 0.0, 1.0).symbol
    }
}
```
