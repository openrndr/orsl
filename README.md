# poc-shader-generation

A set of (future) OPENRNDR extras that facilitate authoring shaders using Kotlin's typesafe builders.

## Installation

```shell
./gradlew publishToMavenLocal snapshot
```

Add `orx-shader-generator` to your openrndr-template project.

## Organization

Module                             | Description
-----------------------------------|-----------------
`orx-glsl-parser`                  | Antlr based GLSL parser
[`orx-shader-generator`](orx-shader-generator/README.md)             | DSLs, shader phrases, shader preprocessor.
`orx-shader-generator-annotations` | 
`orx-shader-generator-processor`   | KSP based annotation processor
