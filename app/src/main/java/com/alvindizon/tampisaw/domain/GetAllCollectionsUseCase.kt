package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toUnsplashCollection
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import io.reactivex.Observable
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getAllCollections(): Observable<PagingData<UnsplashCollection>> {
        return unsplashRepo.getAllCollections().map { response ->
            response.map { it.toUnsplashCollection() }
        }
    }
}
