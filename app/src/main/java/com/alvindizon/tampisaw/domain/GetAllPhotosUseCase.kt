package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.Observable
import javax.inject.Inject

class GetAllPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>> = unsplashRepo.getAllPhotos()
}
