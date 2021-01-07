import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.gradleVersions.name) version Plugins.gradleVersions.version
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(LegacyPluginClassPath.androidGradlePlugin)
        classpath(LegacyPluginClassPath.kotlinPlugin)
        classpath(LegacyPluginClassPath.safeArgs)
        classpath(LegacyPluginClassPath.hiltPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
