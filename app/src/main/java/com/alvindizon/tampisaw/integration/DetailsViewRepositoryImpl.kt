package com.alvindizon.tampisaw.integration

import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.details.integration.DetailsViewRepository
import com.alvindizon.tampisaw.details.model.PhotoDetails
import com.alvindizon.tampisaw.domain.UnsplashRepo
import io.reactivex.rxjava3.core.Single

class DetailsViewRepositoryImpl(private val unsplashRepo: UnsplashRepo) : DetailsViewRepository {

    override fun getPhotoDetails(id: String): Single<PhotoDetails> {
        return unsplashRepo.getPhoto(id).map {
            it.toPhotoDetails()
        }
    }
}
