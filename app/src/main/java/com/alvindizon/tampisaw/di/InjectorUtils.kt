package com.alvindizon.tampisaw.di

import androidx.fragment.app.FragmentActivity
import com.alvindizon.tampisaw.TampisawApp
import com.alvindizon.tampisaw.di.component.AppComponent
import com.alvindizon.tampisaw.di.component.PresentationComponent
import com.alvindizon.tampisaw.di.module.ActivityModule


object InjectorUtils {

    @JvmStatic
    fun getAppComponent(): AppComponent = TampisawApp.getAppComponent()

    @JvmStatic
    fun getPresentationComponent(activity: FragmentActivity): PresentationComponent =
        getAppComponent().presentationComponent(ActivityModule(activity))

}