package com.alvindizon.tampisaw.di.module

import com.alvindizon.tampisaw.data.UnsplashRepoImpl
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.alvindizon.tampisaw.domain.UnsplashRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideUnsplashRepo(unsplashApi: UnsplashApi): UnsplashRepo
            = UnsplashRepoImpl(unsplashApi)
}
