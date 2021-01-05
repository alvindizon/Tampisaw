package com.alvindizon.tampisaw

import android.app.Application
import com.alvindizon.tampisaw.di.component.AppComponent
import com.alvindizon.tampisaw.di.component.DaggerAppComponent

class TampisawApp : Application() {

    companion object {
        private var INSTANCE: TampisawApp? = null

        @JvmStatic
        fun get(): TampisawApp = INSTANCE!!

        @JvmStatic
        fun getAppComponent() = get().appComponent
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appComponent = DaggerAppComponent.builder().application(this).build()
    }
}