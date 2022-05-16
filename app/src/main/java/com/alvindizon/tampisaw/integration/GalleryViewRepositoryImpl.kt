package com.alvindizon.tampisaw.integration

import androidx.paging.PagingData
import androidx.paging.map
import com.alvindizon.tampisaw.core.toPhoto
import com.alvindizon.tampisaw.gallery.integration.GalleryViewRepository
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.repo.UnsplashRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryViewRepositoryImpl @Inject constructor(private val unsplashRepo: UnsplashRepo) :
    GalleryViewRepository {

    override fun getAllPhotos(): Observable<PagingData<Photo>> {
        return unsplashRepo.getAllPhotos().map { response ->
            response.map { it.toPhoto() }
        }
    }
}
