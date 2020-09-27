package com.alvindizon.tampisaw.data.networking.api

import com.alvindizon.tampisaw.BuildConfig
import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

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

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @GET("photos/{id}")
    fun getPhoto(
        @Path("id") id: String
    ): Single<GetPhotoResponse>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Single<ResponseBody>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @GET("photos/{id}/download")
    fun trackDownload(@Path("id") id: String): Completable

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @GET("collections")
    fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<List<GetCollectionsResponse>>

}
