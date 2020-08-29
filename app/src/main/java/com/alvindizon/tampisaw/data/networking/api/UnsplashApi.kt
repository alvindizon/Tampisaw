package com.alvindizon.tampisaw.data.networking.api

import com.alvindizon.tampisaw.BuildConfig
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        const val API_URL = "https://api.unsplash.com/"
        const val ACCESS_KEY = BuildConfig.ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @GET("photos")
    fun getAllPhotos(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order_by") order: String
    ): Single<List<ListPhotosResponse>>

}
