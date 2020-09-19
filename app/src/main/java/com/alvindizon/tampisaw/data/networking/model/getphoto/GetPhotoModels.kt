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
    @Json(name = "current_user_collections")
    val currentUserCollections: List<CurrentUserCollection>? = null,
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
    val model: String? = null,
    @Json(name = "exposure_time")
    val exposureTime: String? = null,
    @Json(name = "aperture")
    val aperture: String? = null,
    @Json(name = "focal_length")
    val focalLength: String? = null,
    @Json(name = "iso")
    val iso: Int? = null
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "city")
    val city: String? = null,
    @Json(name = "country")
    val country: String? = null,
    @Json(name = "position")
    val position: Position
)

@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "latitude")
    val latitude: Double? = null,
    @Json(name = "longitude")
    val longitude: Double? = null,
)

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "title")
    val title: String?,
)

@JsonClass(generateAdapter = true)
data class CurrentUserCollection(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "title")
    val title: String ? = null,
    @Json(name = "published_at")
    val publishedAt: String? = null,
    @Json(name = "last_collected_at")
    val lastCollectedAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String ? = null,
    @Json(name = "cover_photo")
    val coverPhoto: Any? = null,
    @Json(name = "user")
    val user: Any? = null
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
    val thumb: String,
)

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "self")
    val self: String? = null,
    @Json(name = "html")
    val html: String? = null,
    @Json(name = "download")
    val download: String? = null,
    @Json(name = "download_location")
    val downloadLocation: String? = null,
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "portfolio_url")
    val portfolioUrl: String? = null,
    @Json(name = "bio")
    val bio: String? = null,
    @Json(name = "location")
    val location: String? = null,
    @Json(name = "total_likes")
    val totalLikes: Int = 0,
    @Json(name = "total_photos")
    val totalPhotos: Int = 0,
    @Json(name = "total_collections")
    val totalCollections: Int = 0,
    @Json(name = "links")
    val links: UserLinks,
    @Json(name = "profile_image")
    val profileImage: ProfileImage? = null,
)

@JsonClass(generateAdapter = true)
data class UserLinks(
    @Json(name = "self")
    val self: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "photos")
    val photos: String,
    @Json(name = "likes")
    val likes: String,
    @Json(name = "portfolio")
    val portfolio: String
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