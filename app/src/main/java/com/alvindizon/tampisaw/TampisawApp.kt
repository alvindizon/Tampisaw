package com.alvindizon.tampisaw

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.alvindizon.tampisaw.di.app.AppComponent
import com.alvindizon.tampisaw.di.app.DaggerAppComponent

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
        initWorkManager()
    }

    private fun initWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(getAppComponent().listenableWorkerFactory())
            .build()
        WorkManager.initialize(this, config)
    }
}