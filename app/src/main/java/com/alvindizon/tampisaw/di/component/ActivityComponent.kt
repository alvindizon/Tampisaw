package com.alvindizon.tampisaw.di.component

import androidx.appcompat.app.AppCompatActivity
import com.alvindizon.tampisaw.di.module.ActivityModule
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun presentationComponent(): PresentationComponent

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: AppCompatActivity): Builder
        fun build(): ActivityComponent
    }
}