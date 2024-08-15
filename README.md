# ORSL

![orsl in maven](https://maven-badges.herokuapp.com/maven-central/org.openrndr.orsl/orsl-shader-generator-jvm/badge.svg).

A set of modules that facilitate authoring shaders using Kotlin's typesafe builders. ORSL depends on [OPENRNDR](github.com/openrndr/openrndr) and
[ORX](github.com/openrndr/orx).

## Examples

Take a look at the programs under the [demos folder](https://github.com/openrndr/orsl/tree/master/orsl-demos/src/main/kotlin) to get started.
Screenshots of the programs can be found in [this post](https://openrndr.discourse.group/t/kotlin-based-shader-language/563/10).

## Usage

The `next-version` branch of the [openrndr-template](https://github.com/openrndr/openrndr-template/) includes ORSL.
Simply uncomment the `ORSL dependencies` section in the `build.gradle.kts` file and you are ready use ORSL features in your template.

## Contributing

The simplest way to experiment with ORSL is to clone this repo and play with the programs in the [demos folder](https://github.com/openrndr/orsl/tree/master/orsl-demos/src/main/kotlin).

## Built a SNAPSHOT

If you want to customize ORSL and use it in your local template-based programs:

1. Clone this repo
2. Build a SNAPSHOT
```shell
./gradlew publishToMavenLocal snapshot
```
3. Go to your copy [openrndr-template](https://github.com/openrndr/openrndr-template/)
4. Switch to the `next-version` branch.
5. Make sure `orsl` in `gradle/libs.versions.toml` points to the SNAPSHOT version you just built on step 2.
6. Uncomment the `ORSL dependencies` section in the `build.gradle.kts` file.

## Organization

| Module                                                     | Description                                |
|------------------------------------------------------------|--------------------------------------------|
| `orsl-glsl-parser`                                         | Antlr based GLSL parser                    |
| [`orsl-shader-generator`](orsl-shader-generator/README.md) | DSLs, shader phrases, shader preprocessor. |
| `orsl-shader-generator-annotations`                        |                                            |
| `orsl-shader-generator-processor`                          | KSP based annotation processor             |
