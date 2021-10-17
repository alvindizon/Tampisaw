package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun searchPhotos(query: String): Observable<PagingData<UnsplashPhoto>> {
        return unsplashRepo.searchPhotos(query)
            .map { response ->
                response.map { it.toUnsplashPhoto() }
            }
    }
}
