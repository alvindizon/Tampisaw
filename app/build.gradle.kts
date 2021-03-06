import java.util.Properties
import java.io.FileInputStream

plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kapt)
    id(Plugins.kotlinNavSafeArgs)
    id(Plugins.hilt)
}

fun getAccessKey(): String {
    val properties = Properties()
    val fis = FileInputStream("local.properties")
    properties.load(fis)
    val applicationKey = properties.getProperty("ACCESS_KEY")
    if (applicationKey.isNullOrBlank()) {
        throw IllegalStateException("Set your Unsplash API access key in local.properties file. Get your own Unsplash API Access key from here : https://unsplash.com/oauth/applications")
    }
    return applicationKey
}

android {
    compileSdkVersion(Configs.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Configs.minSdkVersion)
        targetSdkVersion(Configs.targetSdkVersion)
        applicationId = Configs.applicationId
        versionCode = Configs.versionCode
        versionName = Configs.versionName
        testInstrumentationRunner = Configs.testInstrumentationRunner
        buildConfigField ("String", "ACCESS_KEY", "\"${getAccessKey()}\"")
    }
    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    Libs.implementations.forEach {
        implementation(it)
    }

    Libs.androidTestImplementations.forEach {
        androidTestImplementation(it)
    }

    Libs.testImplementations.forEach {
        testImplementation(it)
    }

    Libs.kaptLibs.forEach {
        kapt(it)
    }

    api(Libs.dagger)
}
