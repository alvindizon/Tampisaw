package com.alvindizon.tampisaw.data.networking.model.getphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPhotoResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "width")
    val width: Int = 0,
    @Json(name = "height")
    val height: Int = 0,
    @Json(name = "color")
    val color: String,
    @Json(name = "downloads")
    val downloads: Int = 0,
    @Json(name = "views")
    val views: Int = 0,
    @Json(name = "likes")
    val likes: Int = 0,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean = false,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "exif")
    val exif: Exif = Exif(),
    @Json(name = "location")
    val location: Location,
    @Json(name = "tags")
    val tags: List<Tag>? = null,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "links")
    val links: Links,
    @Json(name = "user")
    val user: User
)

@JsonClass(generateAdapter = true)
data class Exif(
    @Json(name = "make")
    val make: String? = null,
    @Json(name = "model")
    val model: String? = null
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "city")
    val city: String? = null,
    @Json(name = "country")
    val country: String? = null
)

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "title")
    val title: String?
)

@JsonClass(generateAdapter = true)
data class Urls(
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

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "self")
    val self: String? = null,
    @Json(name = "download")
    val download: String? = null,
    @Json(name = "download_location")
    val downloadLocation: String? = null
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    @Json(name = "large")
    val large: String
)
