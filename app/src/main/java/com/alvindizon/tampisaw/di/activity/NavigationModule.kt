package com.alvindizon.tampisaw.di.activity

import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.alvindizon.tampisaw.navigation.GalleryNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule {
    @Binds
    abstract fun provideGalleryNavigator(galleryNavigatorImpl: GalleryNavigatorImpl): GalleryNavigator
}
