object Configs {
    const val applicationId = "com.alvindizon.tampisaw"
    const val compileSdkVersion = 30
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val versionCode = 1
    const val versionName = "1.0.0"
}
object LegacyPluginClassPath {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val kapt = "kapt"
    const val kotlinAndroid = "android"
    const val kotlinNavSafeArgs = "androidx.navigation.safeargs.kotlin"
    val gradleVersions = PluginClass("com.github.ben-manes.versions", Versions.gradleVersions)
}

data class PluginClass(val name: String, val version: String)
