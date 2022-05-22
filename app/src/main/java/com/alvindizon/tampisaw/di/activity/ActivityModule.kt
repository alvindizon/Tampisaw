package com.alvindizon.tampisaw.di.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alvindizon.tampisaw.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    // Hilt provides Activity implicitly, but not AppCompatActivity
    // need to create a separate @Provide annotated method to provide AppCompatActivity
    @Provides
    @ActivityScoped
    fun provideAppCompatActivity(activity: Activity) = activity as AppCompatActivity

    @Provides
    @ActivityScoped
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    fun provideNavController(activity: FragmentActivity): NavController =
        NavHostFragment.findNavController(
            activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_txn)!!
        )
}
