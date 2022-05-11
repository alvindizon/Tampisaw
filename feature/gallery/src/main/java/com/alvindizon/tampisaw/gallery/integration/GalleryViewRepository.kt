package com.alvindizon.tampisaw.gallery.integration

import androidx.paging.PagingData
import com.alvindizon.tampisaw.gallery.model.Photo
import io.reactivex.rxjava3.core.Observable

interface GalleryViewRepository {

    fun getAllPhotos(): Observable<PagingData<Photo>>
}
