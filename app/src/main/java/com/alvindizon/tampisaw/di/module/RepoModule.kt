package com.alvindizon.tampisaw.di.module

import com.alvindizon.tampisaw.data.UnsplashRepoImpl
import com.alvindizon.tampisaw.data.paging.UnsplashPagingSource
import com.alvindizon.tampisaw.domain.UnsplashRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideUnsplashRepo(unsplashPagingSource: UnsplashPagingSource): UnsplashRepo
            = UnsplashRepoImpl(unsplashPagingSource)
}
