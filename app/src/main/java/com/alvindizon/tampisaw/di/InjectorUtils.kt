package com.alvindizon.tampisaw.di

import com.alvindizon.tampisaw.TampisawApp
import com.alvindizon.tampisaw.di.component.AppComponent
import com.alvindizon.tampisaw.di.component.PresentationComponent


object InjectorUtils {

    @JvmStatic
    fun getAppComponent(): AppComponent = TampisawApp.getAppComponent()

    @JvmStatic
    fun getPresentationComponent(): PresentationComponent =
        getAppComponent().presentationComponent()

}