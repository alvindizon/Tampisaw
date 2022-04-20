package com.alvindizon.tampisaw.di.app

import com.alvindizon.tampisaw.api.UnsplashApi
import com.alvindizon.tampisaw.data.UnsplashRepoImpl
import com.alvindizon.tampisaw.domain.UnsplashRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun provideUnsplashRepo(unsplashApi: UnsplashApi): UnsplashRepo = UnsplashRepoImpl(unsplashApi)
}
