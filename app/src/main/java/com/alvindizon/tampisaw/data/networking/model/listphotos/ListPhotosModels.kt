package com.alvindizon.tampisaw.data.networking.model.listphotos


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListPhotosResponse(
    @Json(name = "color")
    val color: String,
    @Json(name = "sponsorship")
    val sponsorship: Sponsorship?,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int
)

@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "full")
    val full: String? = null,
    @Json(name = "raw")
    val raw: String? = null,
    @Json(name = "regular")
    val regular: String? = null,
    @Json(name = "small")
    val small: String? = null,
    @Json(name = "thumb")
    val thumb: String? = null
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String,
    @Json(name = "location")
    val location: String? = null,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null,
    @Json(name = "username")
    val username: String
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    @Json(name = "large")
    val large: String
)

@JsonClass(generateAdapter = true)
data class Sponsorship(
    val sponsor: User?
)

