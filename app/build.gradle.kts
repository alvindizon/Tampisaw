import java.util.Properties
import java.io.FileInputStream

plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kapt)
    id(Plugins.kotlinNavSafeArgs)
    id(Plugins.hilt)
    id("kotlin-parcelize")
    id(Plugins.googleServices)
    id(Plugins.crashlytics)
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
    compileSdk = Configs.compileSdkVersion
    defaultConfig {
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion
        applicationId = Configs.applicationId
        versionCode = Configs.versionCode
        versionName = Configs.versionName
        testInstrumentationRunner = Configs.testInstrumentationRunner
        buildConfigField ("String", "ACCESS_KEY", "\"${getAccessKey()}\"")
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}

dependencies {
    implementation(platform(Libs.firebaseBom))

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

    debugImplementation(Libs.leakCanary)
}
