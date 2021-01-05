package com.alvindizon.tampisaw.di.component

import androidx.fragment.app.FragmentActivity
import com.alvindizon.tampisaw.di.module.ActivityModule
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun presentationComponent(): PresentationComponent

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: FragmentActivity): Builder
        fun build(): ActivityComponent
    }
}