package com.alvindizon.tampisaw.di.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ListenableWorkerFactory @Inject constructor(
    private val workerComponent: WorkerComponent.Builder
) : WorkerFactory() {

    private fun createWorker(
        workerClassName: String,
        workers: Map<Class<out ListenableWorker>, Provider<ListenableWorker>>
    ): ListenableWorker? {
        try {
            val workerClass =
                Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
            var provider = workers[workerClass]
            if (provider == null) {
                for ((key, value) in workers) {
                    if (workerClass.isAssignableFrom(key)) {
                        provider = value
                        break
                    }
                }
            }

            requireNotNull(provider) {
                throw IllegalArgumentException("unknown model class $workerClassName")
            }

            return provider.get()

        } catch (e: Exception) {
            return null
        }
    }

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ) = workerComponent
        .workerParameters(workerParameters)
        .build()
        .run { createWorker(workerClassName, workers()) }
}