package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetAllPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>> {
        return unsplashRepo.getAllPhotos()
            .map { response ->
                response.map { it.toUnsplashPhoto() }
            }
    }
}
