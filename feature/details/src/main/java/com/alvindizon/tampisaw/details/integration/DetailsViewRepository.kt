package com.alvindizon.tampisaw.details.integration

import com.alvindizon.tampisaw.details.model.PhotoDetails
import io.reactivex.rxjava3.core.Single

interface DetailsViewRepository {

    fun getPhotoDetails(id: String) : Single<PhotoDetails>
}
