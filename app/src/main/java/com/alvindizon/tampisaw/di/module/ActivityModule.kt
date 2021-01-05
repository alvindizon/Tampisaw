package com.alvindizon.tampisaw.di.module

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.alvindizon.tampisaw.core.ui.DialogManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideFragmentManager(activity: FragmentActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    fun provideDialogManager(fragmentManager: FragmentManager): DialogManager =
        DialogManager(fragmentManager)
}
