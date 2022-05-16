package com.alvindizon.tampisaw.di.app

import com.alvindizon.tampisaw.collections.integration.CollectionsViewRepository
import com.alvindizon.tampisaw.details.integration.DetailsViewRepository
import com.alvindizon.tampisaw.gallery.integration.GalleryViewRepository
import com.alvindizon.tampisaw.integration.CollectionsViewRepositoryImpl
import com.alvindizon.tampisaw.integration.DetailsViewRepositoryImpl
import com.alvindizon.tampisaw.integration.GalleryViewRepositoryImpl
import com.alvindizon.tampisaw.integration.SearchViewRepositoryImpl
import com.alvindizon.tampisaw.repo.UnsplashRepo
import com.alvindizon.tampisaw.repo.UnsplashRepoImpl
import com.alvindizon.tampisaw.search.integration.SearchViewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideUnsplashRepo(repo: UnsplashRepoImpl): UnsplashRepo

    @Binds
    abstract fun provideCollectionsViewRepo(repo: CollectionsViewRepositoryImpl): CollectionsViewRepository

    @Binds
    abstract fun provideDetailsViewRepo(repo: DetailsViewRepositoryImpl): DetailsViewRepository

    @Binds
    abstract fun provideGalleryViewRepo(repo: GalleryViewRepositoryImpl): GalleryViewRepository

    @Binds
    abstract fun provideSearchViewRepo(repo: SearchViewRepositoryImpl): SearchViewRepository
}
