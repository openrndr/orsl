rootProject.name = "poc-shader-generation"
include(
    listOf("orx-shader-generator", "orx-shader-generator-annotations", "orx-shader-generator-processor",
    "orx-jvm:orx-glsl-parser")

)
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
