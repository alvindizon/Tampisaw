package com.alvindizon.tampisaw.integration

import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.details.integration.DetailsViewRepository
import com.alvindizon.tampisaw.details.model.PhotoDetails
import com.alvindizon.tampisaw.repo.UnsplashRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsViewRepositoryImpl @Inject constructor(private val unsplashRepo: UnsplashRepo) :
    DetailsViewRepository {

    override fun getPhotoDetails(id: String): Single<PhotoDetails> {
        return unsplashRepo.getPhoto(id).map {
            it.toPhotoDetails()
        }
    }
}
