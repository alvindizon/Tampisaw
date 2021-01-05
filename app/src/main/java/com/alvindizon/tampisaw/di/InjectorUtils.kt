package com.alvindizon.tampisaw.di

import androidx.fragment.app.FragmentActivity
import com.alvindizon.tampisaw.TampisawApp
import com.alvindizon.tampisaw.di.component.ActivityComponent
import com.alvindizon.tampisaw.di.component.AppComponent
import com.alvindizon.tampisaw.di.component.PresentationComponent


object InjectorUtils {

    @JvmStatic
    fun getAppComponent(): AppComponent = TampisawApp.getAppComponent()

    @JvmStatic
    fun getActivityComponent(activity: FragmentActivity): ActivityComponent =
        getAppComponent().activityComponentBuilder().activity(activity).build()

    @JvmStatic
    fun getPresentationComponent(activity: FragmentActivity): PresentationComponent =
        getActivityComponent(activity).presentationComponent()

}