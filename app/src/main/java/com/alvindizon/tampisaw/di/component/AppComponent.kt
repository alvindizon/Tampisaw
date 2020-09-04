package com.alvindizon.tampisaw.di.component


import com.alvindizon.tampisaw.di.module.ActivityModule
import com.alvindizon.tampisaw.di.module.AppModule
import com.alvindizon.tampisaw.di.module.NetworkModule
import com.alvindizon.tampisaw.di.module.RepoModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class])
@Singleton
interface AppComponent {

    fun presentationComponent(activityModule: ActivityModule): PresentationComponent

}