# orx-shader-generator

Tools for authoring shaders using Kotlin's typesafe builders

## Shadestyle extensions

```kotlin
fun main() = application {
    program {
        extend(Camera2D())
        extend {
            drawer.shadeStyle = shadeStyle {
                fragmentTransform {
                    val p_time by parameter<Double>()
                    val va_texCoord0 by parameter<Vector2>()
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

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by parameter<Vector2>()
    val f by function<Double, Double> { x ->
        x * x + cos(p_time)
    } 
    val d by f(va_texCoord0.x)
    x_fill = Vector4(d, d, d, 1.0)
}
```

## Globals 

```kotlin
fragmentTransform {
    val p_time by parameter<Double>()
    val va_texCoord0 by parameter<Vector2>()
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
    val va_texCoord0 by parameter<Vector2>()
    
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