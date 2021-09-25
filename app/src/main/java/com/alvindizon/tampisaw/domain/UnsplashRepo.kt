package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import io.reactivex.Observable
import io.reactivex.Single

interface UnsplashRepo {

    fun getAllPhotos(): Observable<PagingData<ListPhotosResponse>>

    fun getPhoto(id: String): Single<GetPhotoResponse>

    fun getAllCollections(): Observable<PagingData<GetCollectionsResponse>>

    fun getCollectionPhotos(id: String): Observable<PagingData<ListPhotosResponse>>

    fun searchPhotos(query: String): Observable<PagingData<ListPhotosResponse>>

    fun searchCollections(query: String): Observable<PagingData<GetCollectionsResponse>>
}
