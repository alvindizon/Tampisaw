package com.alvindizon.tampisaw.core.ui

import androidx.appcompat.app.AppCompatActivity
import com.alvindizon.tampisaw.TampisawApp

abstract class BaseActivity: AppCompatActivity() {

    private val appComponent
        get() = TampisawApp.getAppComponent()

    val activityComponent by lazy {
        appComponent.activityComponentBuilder()
            .activity(this)
            .build()
    }

    private val presentationComponent by lazy {
        activityComponent.presentationComponent()
    }

}
