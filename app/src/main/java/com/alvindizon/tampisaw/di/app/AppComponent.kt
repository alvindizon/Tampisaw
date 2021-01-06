package com.alvindizon.tampisaw.di.app


import android.app.Application
import com.alvindizon.tampisaw.di.activity.ActivityComponent
import com.alvindizon.tampisaw.di.worker.ListenableWorkerFactory
import com.alvindizon.tampisaw.di.worker.WorkerComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class])
@Singleton
interface AppComponent {

    fun activityComponentBuilder(): ActivityComponent.Builder

    fun listenableWorkerFactory(): ListenableWorkerFactory

    fun workerComponentBuilder(): WorkerComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}