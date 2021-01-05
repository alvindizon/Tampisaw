package com.alvindizon.tampisaw.di.app


import android.app.Application
import com.alvindizon.tampisaw.di.activity.ActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class, WorkerModule::class])
@Singleton
interface AppComponent {

    fun activityComponentBuilder(): ActivityComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}