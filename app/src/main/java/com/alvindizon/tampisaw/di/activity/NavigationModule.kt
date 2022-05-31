package com.alvindizon.tampisaw.di.activity

import com.alvindizon.tampisaw.details.navigation.DetailsNavigator
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.alvindizon.tampisaw.navigation.DetailsNavigatorImpl
import com.alvindizon.tampisaw.navigation.GalleryNavigatorImpl
import com.alvindizon.tampisaw.navigation.SearchNavigatorImpl
import com.alvindizon.tampisaw.search.navigation.SearchNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule {
    @Binds
    abstract fun provideGalleryNavigator(galleryNavigatorImpl: GalleryNavigatorImpl): GalleryNavigator

    @Binds
    abstract fun provideSearchNavigator(searchNavigatorImpl: SearchNavigatorImpl): SearchNavigator

    @Binds
    abstract fun provideDetailsNavigator(detailsNavigator: DetailsNavigatorImpl): DetailsNavigator

}
