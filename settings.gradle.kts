rootProject.name = "orsl"
include(
    listOf(
        "orsl-demos",
        "orsl-extension-color",
        "orsl-extension-easing",
        "orsl-extension-gradient",
        "orsl-extension-sdf",
        "orsl-extension-noise",
        "orsl-extension-pbr",
        "orsl-extension-raymarching",
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
