package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toUnsplashCollection
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import io.reactivex.Observable
import javax.inject.Inject

class SearchCollectionsUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun searchCollections(query: String): Observable<PagingData<UnsplashCollection>> {
        return unsplashRepo.searchCollections(query)
            .map { response -> response.map { it.toUnsplashCollection() } }
    }
}
