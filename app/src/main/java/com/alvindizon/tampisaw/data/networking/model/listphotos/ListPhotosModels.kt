package com.alvindizon.tampisaw.data.networking.model.listphotos


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListPhotosResponse(
    @Json(name = "color")
    val color: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<CurrentUserCollection?>? = null,
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
    @Json(name = "links")
    val links: Links,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int,
)

@JsonClass(generateAdapter = true)
data class CurrentUserCollection(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "last_collected_at")
    val lastCollectedAt: String? = null,
    @Json(name = "published_at")
    val publishedAt: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "download")
    val download: String? = null,
    @Json(name = "download_location")
    val downloadLocation: String? = null,
    @Json(name = "html")
    val html: String? = null,
    @Json(name = "self")
    val self: String? = null
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
    @Json(name = "bio")
    val bio: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "instagram_username")
    val instagramUsername: String? = null,
    @Json(name = "links")
    val links: UserLinks,
    @Json(name = "location")
    val location: String? = null,
    @Json(name = "name")
    val name: String,
    @Json(name = "portfolio_url")
    val portfolioUrl: String? = null,
    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null,
    @Json(name = "total_collections")
    val totalCollections: Int,
    @Json(name = "total_likes")
    val totalLikes: Int,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "twitter_username")
    val twitterUsername: String? = null,
    @Json(name = "username")
    val username: String
)

@JsonClass(generateAdapter = true)
data class UserLinks(
    @Json(name = "html")
    val html: String,
    @Json(name = "likes")
    val likes: String,
    @Json(name = "photos")
    val photos: String,
    @Json(name = "portfolio")
    val portfolio: String,
    @Json(name = "self")
    val self: String
)

@JsonClass(generateAdapter = true)
data class ProfileImage(
    @Json(name = "large")
    val large: String,
    @Json(name = "medium")
    val medium: String,
    @Json(name = "small")
    val small: String
)

