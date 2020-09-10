package com.alvindizon.tampisaw.di.component


import com.alvindizon.tampisaw.di.module.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class, WorkerModule::class])
@Singleton
interface AppComponent {

    fun presentationComponent(activityModule: ActivityModule): PresentationComponent

}