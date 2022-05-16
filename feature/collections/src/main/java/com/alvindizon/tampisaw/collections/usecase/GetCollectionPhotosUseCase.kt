package com.alvindizon.tampisaw.collections.usecase

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.integration.CollectionsViewRepository
import com.alvindizon.tampisaw.gallery.model.Photo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetCollectionPhotosUseCase @Inject constructor(private val repo: CollectionsViewRepository) {

    fun getCollectionPhotos(id: String): Observable<PagingData<Photo>> =
        repo.getCollectionPhotos(id)
}
