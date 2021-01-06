package com.alvindizon.tampisaw.di.worker

import androidx.work.ListenableWorker
import com.alvindizon.tampisaw.data.download.ImageDownloader
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class WorkerModule {
    @MapKey
    annotation class WorkerKey(val value: KClass<out ListenableWorker>)


    @Binds
    @IntoMap
    @WorkerKey(ImageDownloader::class)
    abstract fun provideImageDownload(imageDownloader: ImageDownloader): ListenableWorker
}
