rootProject.name = "orsl"
include(
    listOf(
        "orsl-shader-generator",
        "orsl-shader-generator-annotations",
        "orsl-shader-generator-processor",
        "orsl-jvm:orsl-glsl-parser"
    )

)
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
