plugins {
    org.openrndr.orsl.convention.`kotlin-jvm`
}

dependencies {
    implementation(libs.openrndr.application)
    implementation(libs.openrndr.gl3.core)
    implementation(project(":orsl-shader-generator"))
    implementation(project(":orsl-extension-color"))
    implementation(project(":orsl-extension-easing"))
    implementation(project(":orsl-extension-gradient"))
    implementation(project(":orsl-extension-noise"))
    implementation(project(":orsl-extension-pbr"))
    implementation(project(":orsl-extension-raymarching"))
    implementation(project(":orsl-extension-sdf"))

    implementation(libs.orx.camera)
    implementation(libs.orx.mesh.generators)
    implementation(libs.orx.olive)

    testImplementation(libs.kluent)
}