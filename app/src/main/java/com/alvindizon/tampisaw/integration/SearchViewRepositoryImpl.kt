package com.alvindizon.tampisaw.integration

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.collections.model.Collection
import com.alvindizon.tampisaw.core.toCollection
import com.alvindizon.tampisaw.core.toPhoto
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.repo.UnsplashRepo
import com.alvindizon.tampisaw.search.integration.SearchViewRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchViewRepositoryImpl @Inject constructor(private val unsplashRepo: UnsplashRepo) :
    SearchViewRepository {

    override fun searchPhotos(query: String): Observable<PagingData<Photo>> {
        return unsplashRepo.searchPhotos(query)
            .map { response ->
                response.map { it.toPhoto() }
            }
    }

    override fun searchCollections(query: String): Observable<PagingData<Collection>> {
        return unsplashRepo.searchCollections(query)
            .map { response ->
                response.map { it.toCollection() }
            }
    }
}
