package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun searchPhotos(query: String): Observable<PagingData<UnsplashPhoto>> = unsplashRepo.searchPhotos(query)
}