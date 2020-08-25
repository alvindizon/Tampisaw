package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable

interface UnsplashRepo {

    fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>>
}