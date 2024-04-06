import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    org.openrndr.orsl.convention.`kotlin-multiplatform`
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"

}

kotlin {
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(libs.openrndr.application)
                implementation(libs.openrndr.draw)
                implementation(libs.openrndr.filter)

                implementation(libs.kotlin.reflect)
                implementation(project(":orsl-shader-generator-annotations"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmDemo by getting {
            dependencies {
                implementation(libs.openrndr.ffmpeg)
                implementation(libs.orx.mesh.generators)
                implementation(libs.orx.camera)
                implementation(libs.orx.olive)
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(libs.openrndr.gl3.core)
                implementation(libs.kotlin.coroutines)
                implementation(libs.orx.mesh.generators)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":orsl-shader-generator-processor"))
}

tasks.withType<KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
tasks.findByName("jsSourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")
tasks.findByName("jvmSourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")
tasks.findByName("sourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
