package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.Observable
import javax.inject.Inject

class GetCollectionPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getCollectionPhotos(id: String): Observable<PagingData<UnsplashPhoto>> {
        return unsplashRepo.getCollectionPhotos(id).map { response ->
            response.map { it.toUnsplashPhoto() }
        }
    }
}
