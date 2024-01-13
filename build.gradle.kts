group = "org.openrndr.orsl"
plugins {
    alias(libs.plugins.nebula.release)
    alias(libs.plugins.gradle.nexus.publish)
}

repositories {
    mavenCentral()
}

nexusPublishing {
    repositories {
        sonatype {
            username.set((findProperty("ossrhUsername") as String?) ?: System.getenv("OSSRH_USERNAME"))
            password.set((findProperty("ossrhPassword") as String?) ?: System.getenv("OSSRH_PASSWORD"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots"))
        }
    }
}
