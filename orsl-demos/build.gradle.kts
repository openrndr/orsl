plugins {
    org.openrndr.orsl.convention.`kotlin-jvm`
}

dependencies {
    implementation(libs.openrndr.application)
    implementation(libs.openrndr.gl3.core)
    implementation("org.openrndr:openrndr-gl3-natives-macos-arm64:0.4.5-SNAPSHOT")
    implementation(project(":orsl-shader-generator"))
    implementation(project(":orsl-extension-color"))
    implementation(project(":orsl-extension-easing"))
    implementation(project(":orsl-extension-gradient"))
    implementation(project(":orsl-extension-noise"))
    implementation(project(":orsl-extension-pbr"))
    implementation(project(":orsl-extension-raymarching"))
    implementation(project(":orsl-extension-sdf"))
    runtimeOnly(libs.slf4j.simple)
    implementation(libs.orx.camera)
    implementation(libs.orx.mesh.generators)
    implementation(libs.orx.olive)
    testImplementation(libs.kluent)
}