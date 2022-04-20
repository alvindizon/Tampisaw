package com.alvindizon.tampisaw.api

import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.api.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.api.model.search.SearchCollectionsResponse
import com.alvindizon.tampisaw.api.model.search.SearchPhotosResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface UnsplashApi {

    @GET("photos")
    fun getAllPhotos(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order_by") order: String
    ): Single<List<ListPhotosResponse>>

    @GET("photos/{id}")
    fun getPhoto(
        @Path("id") id: String
    ): Single<GetPhotoResponse>

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Single<ResponseBody>

    @GET("photos/{id}/download")
    fun trackDownload(@Path("id") id: String): Completable

    @GET("collections")
    fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<List<GetCollectionsResponse>>

    @GET("collections/{id}/photos")
    fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<List<ListPhotosResponse>>

    @GET("search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("order_by") orderBy: String?,
        @Query("collections") collections: String?,
        @Query("content_filter") contentFilter: String?,
        @Query("color") color: String?,
        @Query("orientation") orientation: String?
    ): Single<SearchPhotosResponse>

    @GET("search/collections")
    fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
    ): Single<SearchCollectionsResponse>
}
