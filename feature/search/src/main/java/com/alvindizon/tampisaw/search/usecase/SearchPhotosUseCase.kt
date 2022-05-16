package com.alvindizon.tampisaw.search.usecase

import androidx.paging.PagingData
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.search.integration.SearchViewRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(private val repo: SearchViewRepository) {

    fun searchPhotos(query: String): Observable<PagingData<Photo>> = repo.searchPhotos(query)
}
