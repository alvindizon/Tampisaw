package com.alvindizon.tampisaw.gallery.usecase

import androidx.paging.PagingData
import com.alvindizon.tampisaw.gallery.integration.GalleryViewRepository
import com.alvindizon.tampisaw.gallery.model.Photo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetAllPhotosUseCase @Inject constructor(private val repo: GalleryViewRepository) {

    fun getAllPhotos(): Observable<PagingData<Photo>> = repo.getAllPhotos()
}
