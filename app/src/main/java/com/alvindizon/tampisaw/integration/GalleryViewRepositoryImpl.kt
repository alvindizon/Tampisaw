package com.alvindizon.tampisaw.integration

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toPhoto
import com.alvindizon.tampisaw.domain.UnsplashRepo
import com.alvindizon.tampisaw.gallery.integration.GalleryViewRepository
import com.alvindizon.tampisaw.gallery.model.Photo
import io.reactivex.rxjava3.core.Observable

// TODO create hilt module for providing viewrepos
class GalleryViewRepositoryImpl(private val unsplashRepo: UnsplashRepo): GalleryViewRepository {

    override fun getAllPhotos(): Observable<PagingData<Photo>> {
        return unsplashRepo.getAllPhotos().map { response ->
            response.map { it.toPhoto() }
        }
    }
}
