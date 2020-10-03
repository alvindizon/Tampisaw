package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import io.reactivex.Observable
import io.reactivex.Single

interface UnsplashRepo {

    fun getAllPhotos(): Observable<PagingData<UnsplashPhoto>>

    fun getPhoto(id: String): Single<GetPhotoResponse>

    fun getAllCollections(): Observable<PagingData<UnsplashCollection>>

    fun getCollectionPhotos(id: Int): Observable<PagingData<UnsplashPhoto>>
}