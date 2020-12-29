package com.alvindizon.tampisaw.di.module

import androidx.paging.ExperimentalPagingApi
import com.alvindizon.tampisaw.data.UnsplashRepoImpl
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.domain.UnsplashRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @ExperimentalPagingApi
    @Provides
    @Singleton
    fun provideUnsplashRepo(unsplashApi: UnsplashApi): UnsplashRepo
            = UnsplashRepoImpl(unsplashApi)
}
