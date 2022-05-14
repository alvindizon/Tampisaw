package com.alvindizon.tampisaw.collections.integration

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.model.Collection
import io.reactivex.rxjava3.core.Observable

interface CollectionsViewRepository {

    fun getAllCollections(): Observable<PagingData<Collection>>
}
