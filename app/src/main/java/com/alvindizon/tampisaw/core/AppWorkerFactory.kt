package com.alvindizon.tampisaw.core

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alvindizon.tampisaw.core.ui.NotifsHelper
import com.alvindizon.tampisaw.data.download.ImageDownloader
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi

class AppWorkerFactory(
    private val unsplashApi: UnsplashApi,
    private val notifsHelper: NotifsHelper,
    private val contentResolver: ContentResolver): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
        val constructor = workerClass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
        val instance = constructor.newInstance(appContext, workerParameters)

        when (instance) {
            is ImageDownloader -> injectImageDownloader(instance)
            else -> Log.i("AppWorkerFactory","No injection required for worker $workerClassName")
        }

        return instance
    }

    private fun injectImageDownloader(worker: ImageDownloader) {
        worker.unsplashApi = unsplashApi
        worker.notifsHelper = notifsHelper
        worker.contentResolver = contentResolver
    }
}