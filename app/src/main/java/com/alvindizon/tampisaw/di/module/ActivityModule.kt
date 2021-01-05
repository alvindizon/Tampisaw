package com.alvindizon.tampisaw.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.alvindizon.tampisaw.core.ui.DialogManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    fun provideDialogManager(fragmentManager: FragmentManager): DialogManager =
        DialogManager(fragmentManager)
}
