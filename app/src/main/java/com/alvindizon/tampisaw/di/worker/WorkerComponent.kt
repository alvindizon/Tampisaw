package com.alvindizon.tampisaw.di.worker

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Provider

@Subcomponent(modules = [WorkerModule::class])
interface WorkerComponent {

    fun workers(): Map<Class<out ListenableWorker>, Provider<ListenableWorker>>

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun workerParameters(param: WorkerParameters): Builder
        fun build(): WorkerComponent
    }
}