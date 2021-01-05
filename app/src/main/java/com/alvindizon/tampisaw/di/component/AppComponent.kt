package com.alvindizon.tampisaw.di.component


import android.app.Application
import com.alvindizon.tampisaw.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class, WorkerModule::class])
@Singleton
interface AppComponent {

    fun presentationComponent(activityModule: ActivityModule): PresentationComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}