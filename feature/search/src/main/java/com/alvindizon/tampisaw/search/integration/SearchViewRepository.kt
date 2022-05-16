package com.alvindizon.tampisaw.search.integration

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.model.Collection
import com.alvindizon.tampisaw.gallery.model.Photo
import io.reactivex.rxjava3.core.Observable

interface SearchViewRepository {

    fun searchPhotos(query: String): Observable<PagingData<Photo>>

    fun searchCollections(query: String): Observable<PagingData<Collection>>
}
