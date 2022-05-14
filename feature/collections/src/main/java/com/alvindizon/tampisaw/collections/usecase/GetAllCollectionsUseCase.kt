package com.alvindizon.tampisaw.collections.usecase

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.integration.CollectionsViewRepository
import com.alvindizon.tampisaw.collections.model.Collection
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(private val repo: CollectionsViewRepository) {

    fun getAllCollections(): Observable<PagingData<Collection>> = repo.getAllCollections()
}
