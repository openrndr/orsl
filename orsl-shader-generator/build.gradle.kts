import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    org.openrndr.orsl.convention.`kotlin-multiplatform`
    // TODO: the next version should not be hardcoded here
    //id(libs.plugins.ksp.annotation.processor.get())
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
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

tasks.withType<KotlinCompile>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
tasks.findByName("jsSourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")
tasks.findByName("jvmSourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")
tasks.findByName("sourcesJar")!!.dependsOn("kspCommonMainKotlinMetadata")
tasks.findByName("compileKotlinJs")!!.dependsOn("kspCommonMainKotlinMetadata")

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
