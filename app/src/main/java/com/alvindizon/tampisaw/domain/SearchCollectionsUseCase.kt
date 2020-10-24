package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import io.reactivex.Observable
import javax.inject.Inject

class SearchCollectionsUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun searchCollections(query: String): Observable<PagingData<UnsplashCollection>> = unsplashRepo.searchCollections(query)
}