package com.alvindizon.tampisaw.di.app

import androidx.paging.ExperimentalPagingApi
import com.alvindizon.tampisaw.data.UnsplashRepoImpl
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.domain.UnsplashRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @ExperimentalPagingApi
    @Provides
    @Singleton
    fun provideUnsplashRepo(unsplashApi: UnsplashApi): UnsplashRepo = UnsplashRepoImpl(unsplashApi)
}
