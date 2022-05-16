package com.alvindizon.tampisaw.search.usecase

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.model.Collection
import com.alvindizon.tampisaw.search.integration.SearchViewRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchCollectionsUseCase @Inject constructor(private val repo: SearchViewRepository) {

    fun searchCollections(query: String): Observable<PagingData<Collection>> =
        repo.searchCollections(query)
}
