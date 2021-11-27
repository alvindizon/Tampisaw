import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.gradleVersions.name) version Plugins.gradleVersions.version
    id(Plugins.detekt.name) version Plugins.detekt.version
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(LegacyPluginClassPath.androidGradlePlugin)
        classpath(LegacyPluginClassPath.kotlinPlugin)
        classpath(LegacyPluginClassPath.safeArgs)
        classpath(LegacyPluginClassPath.hiltPlugin)
        classpath(LegacyPluginClassPath.googleServices)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin(Plugins.detekt.name)
    }

    detekt {
        config = files("$rootDir/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
            xml.enabled = false
            txt.enabled = false
            sarif.enabled = false
        }
    }
}

tasks {
    named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
        rejectVersionIf {
            isNonStable(candidate.version)
        }
        // optional parameters
        checkForGradleUpdate = true
        outputFormatter = "html"
        outputDir = "build/dependencyUpdates"
        reportfileName = "report"
    }

    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }

    withType<Detekt> {
        include("**/*.kt")
        include("**/*.kts")
        exclude(".*/resources/.*")
        exclude(".*/build/.*")
        exclude("/versions.gradle.kts")
        exclude("buildSrc/settings.gradle.kts")
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
