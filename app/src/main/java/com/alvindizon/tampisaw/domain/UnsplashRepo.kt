package com.alvindizon.tampisaw.domain

import androidx.paging.PagingData
import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.api.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface UnsplashRepo {

    fun getAllPhotos(): Observable<PagingData<ListPhotosResponse>>

    fun getPhoto(id: String): Single<GetPhotoResponse>

    fun getAllCollections(): Observable<PagingData<GetCollectionsResponse>>

    fun getCollectionPhotos(id: String): Observable<PagingData<ListPhotosResponse>>

    fun searchPhotos(query: String): Observable<PagingData<ListPhotosResponse>>

    fun searchCollections(query: String): Observable<PagingData<GetCollectionsResponse>>
}
