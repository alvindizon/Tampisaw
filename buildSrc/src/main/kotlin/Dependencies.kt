object Libs {

    val implementations = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}",
        "androidx.core:core-ktx:${Versions.ktx}",
        "androidx.appcompat:appcompat:${Versions.appCompat}",
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}",
        "com.google.android.material:material:${Versions.material}",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}",
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}",
        "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroid}",
        "io.reactivex.rxjava3:rxjava:${Versions.rxJava}",
        "io.reactivex.rxjava3:rxkotlin:${Versions.rxKotlin}",
        "com.google.code.findbugs:jsr305:${Versions.findBugs}",
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}",
        "com.squareup.moshi:moshi:${Versions.moshi}",
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLogInterceptor}",
        "com.github.skydoves:androidveil:${Versions.androidVeil}",
        "com.github.bumptech.glide:glide:${Versions.glide}",
        "androidx.paging:paging-runtime:${Versions.paging}",
        "androidx.paging:paging-rxjava3:${Versions.paging}",
        "androidx.preference:preference:${Versions.preference}",
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "com.nambimobile.widgets:expandable-fab:${Versions.fab}",
        "androidx.work:work-runtime-ktx:${Versions.work}",
        "androidx.work:work-rxjava3:${Versions.work}",
        "com.squareup.okio:okio:${Versions.okio}",
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}",
        "com.google.dagger:hilt-android:${Versions.hilt}",
        "androidx.hilt:hilt-work:${Versions.hiltAndroidX}",
        "com.jakewharton.timber:timber:${Versions.timber}",
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}",
        "androidx.core:core-splashscreen:${Versions.splashScreen}",
        "androidx.concurrent:concurrent-futures-ktx:${Versions.concurrent}",
        "com.google.firebase:firebase-analytics-ktx"
    )

    val androidTestImplementations = listOf(
        "androidx.test.ext:junit:${Versions.testExtJunit}",
        "androidx.test.espresso:espresso-core:${Versions.espresso}",
        "androidx.arch.core:core-testing:${Versions.coreTesting}"
    )

    val testImplementations = listOf(
        "org.junit.jupiter:junit-jupiter:${Versions.junit5}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}",
        "androidx.arch.core:core-testing:${Versions.coreTesting}",
        "io.mockk:mockk:${Versions.mockk}"
    )

    val kaptLibs = listOf(
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}",
        "com.google.dagger:hilt-android-compiler:${Versions.hilt}",
        "androidx.hilt:hilt-compiler:${Versions.hiltAndroidX}"
    )

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebase}"

}

object Versions {
    const val androidGradlePlugin = "7.0.3"
    const val kotlin = "1.5.31"
    const val navigation = "2.3.5"
    const val appCompat = "1.3.1"
    const val constraintLayout = "2.1.1"
    const val espresso = "3.4.0"
    const val glide = "4.12.0"
    const val ktx = "1.6.0"
    const val lifecycle = "2.3.1"
    const val paging = "3.0.1"
    const val retrofit = "2.9.0"
    const val testExtJunit = "1.1.3"
    const val rxJava = "3.1.2"
    const val moshi = "1.12.0"
    const val material = "1.4.0"
    const val findBugs = "3.0.2"
    const val okHttpLogInterceptor = "4.9.2"
    const val androidVeil = "1.1.1"
    const val preference = "1.1.1"
    const val fab = "1.2.1"
    const val work = "2.7.0"
    const val okio = "2.10.0"
    const val swiperefreshlayout = "1.1.0"
    const val coreTesting = "2.1.0"
    const val coroutine = "1.5.2"
    const val rxAndroid = "3.0.0"
    const val rxKotlin = "3.0.1"
    const val gradleVersions = "0.39.0"
    const val mockk = "1.12.0"
    const val junit5 = "5.8.1"
    const val hilt= "2.39.1"
    const val hiltAndroidX = "1.0.0"
    const val timber = "5.0.1"
    const val detekt = "1.18.1"
    const val splashScreen = "1.0.0-alpha01"
    const val leakCanary = "2.7"
    const val concurrent = "1.1.0"
    const val googleServices = "4.3.10"
    const val firebase = "29.0.0"
}
