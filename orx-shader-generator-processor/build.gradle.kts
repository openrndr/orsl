plugins {
    org.openrndr.extra.convention.`kotlin-jvm`
}

dependencies {
    implementation(libs.ksp.symbol.processing)
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlin.poet.ksp)
    implementation(libs.openrndr.application)
    implementation(libs.openrndr.draw)
    implementation(libs.openrndr.filter)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.compiler.embeddable)
    implementation(project(":orx-shader-generator-annotations"))
    implementation(project(":orx-jvm:orx-glsl-parser"))

}

