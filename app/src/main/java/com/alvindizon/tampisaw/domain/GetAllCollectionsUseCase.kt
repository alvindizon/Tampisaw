package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import io.reactivex.Observable
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getAllCollections(): Observable<PagingData<UnsplashCollection>> = unsplashRepo.getAllCollections()
}
