package com.alvindizon.tampisaw.data.networking.model.getcollections


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetCollectionsResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "published_at")
    val publishedAt: String?,
    @Json(name = "last_collected_at")
    val lastCollectedAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "private")
    val `private`: Boolean? = null,
    @Json(name = "share_key")
    val shareKey: String? = null,
    @Json(name = "user")
    val user: User,
    @Json(name = "cover_photo")
    val coverPhoto: CoverPhoto?
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "username")
    val username: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    @Json(name = "small")
    val small: String,
    @Json(name = "medium")
    val medium: String,
    @Json(name = "large")
    val large: String
)

@JsonClass(generateAdapter = true)
data class CoverPhoto(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "width")
    val width: Int? = null,
    @Json(name = "height")
    val height: Int? = null,
    @Json(name = "color")
    val color: String? = null,
    @Json(name = "likes")
    val likes: Int? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "urls")
    val urls: CoverPhotoUrl
)

@JsonClass(generateAdapter = true)
data class CoverPhotoUrl(
    @Json(name = "raw")
    val raw: String,
    @Json(name = "full")
    val full: String,
    @Json(name = "regular")
    val regular: String,
    @Json(name = "small")
    val small: String,
    @Json(name = "thumb")
    val thumb: String
)