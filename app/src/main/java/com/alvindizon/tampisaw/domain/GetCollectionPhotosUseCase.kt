package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import javax.inject.Inject

class GetCollectionPhotosUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getCollectionPhotos(id: Int): Observable<PagingData<UnsplashPhoto>> = unsplashRepo.getCollectionPhotos(id)
}