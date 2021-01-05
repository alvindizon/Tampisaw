package com.alvindizon.tampisaw.di.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.alvindizon.tampisaw.core.ui.DialogManager
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
annotation class ActivityScope

@Module
class ActivityModule {

    @Provides
    @ActivityScope
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    @ActivityScope
    fun provideDialogManager(fragmentManager: FragmentManager): DialogManager =
        DialogManager(fragmentManager)
}
