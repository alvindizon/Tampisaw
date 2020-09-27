package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getAllCollections(): Observable<PagingData<UnsplashCollection>> = unsplashRepo.getAllCollections()
}