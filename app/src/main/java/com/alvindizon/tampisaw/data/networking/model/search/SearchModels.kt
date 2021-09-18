package com.alvindizon.tampisaw.data.networking.model.search

import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPhotosResponse(
        @Json(name = "total")
        val total: Int,
        @Json(name = "total_pages")
        val totalPages: Int,
        @Json(name = "results")
        val results: List<ListPhotosResponse>
)

@JsonClass(generateAdapter = true)
data class SearchCollectionsResponse(
        @Json(name = "total")
        val total: Int,
        @Json(name = "total_pages")
        val totalPages: Int,
        @Json(name = "results")
        val results: List<GetCollectionsResponse>
)
