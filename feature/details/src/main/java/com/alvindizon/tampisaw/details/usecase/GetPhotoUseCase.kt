package com.alvindizon.tampisaw.details.usecase

import com.alvindizon.tampisaw.details.integration.DetailsViewRepository
import com.alvindizon.tampisaw.details.model.PhotoDetails
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(private val repo: DetailsViewRepository) {

    fun getPhoto(id: String): Single<PhotoDetails> = repo.getPhotoDetails(id)
}
