import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    org.openrndr.extra.convention.`kotlin-jvm`
    antlr
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments.addAll(listOf("-visitor", "-long-messages", "-package", "org.openrndr.extra.glslparser.antlr"))
    outputDirectory =  file("${project.buildDir}/generated-src/antlr/org/openrndr/extra/glslparser/antlr".toString())
}

sourceSets {
    main {
        java {
            srcDir("build/generated-src/antlr")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
}

dependencies {
    antlr(libs.antlr.core)
    implementation(libs.antlr.runtime)
    testImplementation(libs.kluent)
}

tasks.getByName("compileKotlin").dependsOn("generateGrammarSource")
tasks.getByName("sourcesJar").dependsOn("generateGrammarSource")
