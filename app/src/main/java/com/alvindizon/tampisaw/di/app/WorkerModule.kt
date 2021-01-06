package com.alvindizon.tampisaw.di.app

import android.content.ContentResolver
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.alvindizon.tampisaw.core.AppWorkerFactory
import com.alvindizon.tampisaw.core.ui.NotifsHelper
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WorkerModule {

    // we do not need WorkManager to be configured automatically when the app starts,
    // so that app startup performance is improved. Thus we configure it manually for
    // on-demand initialization + it enables us to inject it via Dagger
    // See: https://developer.android.com/topic/libraries/architecture/workmanager/advanced/custom-configuration
    @Provides
    @Singleton
    fun provideWorkManager(context: Context, workerFactory: WorkerFactory): WorkManager {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideWorkerFactory(
        unsplashApi: UnsplashApi,
        notifsHelper: NotifsHelper,
        contentResolver: ContentResolver
    ): WorkerFactory {
        return AppWorkerFactory(unsplashApi, notifsHelper, contentResolver)
    }
}