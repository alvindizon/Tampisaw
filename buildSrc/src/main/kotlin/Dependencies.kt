object Libs {

    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"

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
        "androidx.room:room-runtime:${Versions.room}",
        "androidx.room:room-rxjava2:${Versions.room}",
        "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}",
        "io.reactivex.rxjava2:rxjava:${Versions.rxJava}",
        "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}",
        "com.google.code.findbugs:jsr305:${Versions.findBugs}",
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}",
        "com.squareup.moshi:moshi:${Versions.moshi}",
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLogInterceptor}",
        "com.github.skydoves:androidveil:${Versions.androidVeil}",
        "com.github.bumptech.glide:glide:${Versions.glide}",
        "androidx.paging:paging-runtime:${Versions.paging}",
        "androidx.paging:paging-rxjava2:${Versions.paging}",
        "androidx.preference:preference:${Versions.preference}",
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "com.nambimobile.widgets:expandable-fab:${Versions.fab}",
        "androidx.work:work-runtime-ktx:${Versions.work}",
        "androidx.work:work-rxjava2:${Versions.work}",
        "com.squareup.okio:okio:${Versions.okio}",
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    )

    val androidTestImplementations = listOf(
        "androidx.test.ext:junit:${Versions.testExtJunit}",
        "androidx.test.espresso:espresso-core:${Versions.espresso}",
        "androidx.arch.core:core-testing:${Versions.coreTesting}",
        "org.mockito:mockito-android:${Versions.mockito}"
    )

    val testImplementations = listOf(
        "junit:junit:${Versions.junit}",
        "org.mockito:mockito-core:${Versions.mockito}", 
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}",
        "androidx.arch.core:core-testing:${Versions.coreTesting}",
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    )

    val kaptLibs = listOf(
        "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}",
        "com.google.dagger:dagger-compiler:${Versions.dagger}",
        "androidx.room:room-compiler:${Versions.room}"
    )

}

object Versions {
    const val androidGradlePlugin = "4.1.1"
    const val kotlin = "1.4.21"
    const val navigation = "2.3.2"
    const val appCompat = "1.2.0"
    const val constraintLayout = "2.0.4"
    const val espresso = "3.3.0"
    const val glide = "4.11.0"
    const val dagger = "2.30.1"
    const val junit = "4.13.1"
    const val ktx = "1.3.2"
    const val lifecycle = "2.2.0"
    const val paging = "3.0.0-alpha11"
    const val retrofit = "2.9.0"
    const val testExtJunit = "1.1.2"
    const val rxJava = "2.2.20"
    const val moshi = "1.11.0"
    const val material = "1.2.1"
    const val room = "2.3.0-alpha03"
    const val findBugs = "3.0.2"
    const val okHttpLogInterceptor = "4.9.0"
    const val androidVeil = "1.1.1"
    const val preference = "1.1.1"
    const val fab = "1.0.2"
    const val work = "2.4.0"
    const val okio = "2.9.0"
    const val swiperefreshlayout = "1.1.0"
    const val mockito = "3.6.28"
    const val coreTesting = "2.1.0"
    const val coroutine = "1.4.2"
    const val mockitoKotlin = "2.2.0"
    const val rxAndroid = "2.1.1"
    const val rxKotlin = "2.4.0"
    const val gradleVersions = "0.36.0"
}