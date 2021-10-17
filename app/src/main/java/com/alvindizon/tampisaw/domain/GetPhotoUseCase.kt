package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(private val unsplashRepo: UnsplashRepo) {

    fun getPhoto(id: String): Single<GetPhotoResponse> = unsplashRepo.getPhoto(id)
}
