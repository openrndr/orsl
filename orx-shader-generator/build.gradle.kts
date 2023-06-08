import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    org.openrndr.extra.convention.`kotlin-multiplatform`
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"

}

kotlin {


    sourceSets {

        this.forEach {
            println(it.name)
        }

//        val commonKsp by creating {
//            this.kotlin.srcDir("build/generated/metadata/commonMain")
//        }


//        val generatedByKspCommonMainKotlinMetadata by creating { }

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(libs.openrndr.application)
                implementation(libs.openrndr.draw)
                implementation(libs.openrndr.filter)
                implementation(libs.kotlin.reflect)
                implementation(project(":orx-shader-generator-annotations"))
                //api("generatedByKspCommonMainKotlinMetadata")
               // configurations.get("ksp").dependencies.add(project(":orx-shader-generator-processor"))

//                implementation("generatedByKspCommonMainKotlinMetadata")
                //implementation()
            }

        }

        @Suppress("UNUSED_VARIABLE")
        val jvmDemo by getting {
            dependencies {
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
    add("kspCommonMainMetadata", project(":orx-shader-generator-processor"))
//    add("kspJvm", project(":orx-shader-generator-processor"))
//    add("kspJs", project(":orx-shader-generator-processor"))
//    ksp(project(":orx-shader-generator-processor"))
}

/*
dependencies {
    add("kspCommonMainMetadata", project(":test-processor"))
}
*/

tasks.withType<KotlinCompile<*>>().all {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
