# ORSL

A set of modules that facilitate authoring shaders using Kotlin's typesafe builders. ORSL has depends on OPENRNDR and
ORX

## Installation

```shell
./gradlew publishToMavenLocal snapshot
```

Add `orsl-shader-generator` to your openrndr-template project.

## Organization

| Module                                                    | Description                                |
|-----------------------------------------------------------|--------------------------------------------|
| `orsl-glsl-parser`                                        | Antlr based GLSL parser                    |
| [`orsl-shader-generator`](orx-shader-generator/README.md) | DSLs, shader phrases, shader preprocessor. |
| `orsl-shader-generator-annotations`                       |                                            |
| `orsl-shader-generator-processor`                         | KSP based annotation processor             |
