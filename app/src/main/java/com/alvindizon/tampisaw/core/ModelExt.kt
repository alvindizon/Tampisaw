package com.alvindizon.tampisaw.core

import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.api.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.api.model.listphotos.Urls
import com.alvindizon.tampisaw.api.model.listphotos.User
import com.alvindizon.tampisaw.details.model.PhotoDetails
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.gallery.model.PhotoUrls
import com.alvindizon.tampisaw.gallery.model.PhotoUser
import java.util.*


fun ListPhotosResponse.toPhoto() = Photo(
    id, description, user.toPhotoUser(), urls.toPhotoUrls(), sponsorship != null, color,
    height, width
)

fun User.toPhotoUser() = PhotoUser(name, username, profileImage?.large)

fun Urls.toPhotoUrls() = PhotoUrls(raw, full, regular, small, thumb)

fun GetPhotoResponse.toPhotoDetails() = PhotoDetails(
    id, createdAt, updatedAt, "$width x $height", color, views.toCompactFormat(),
    downloads.toCompactFormat(), likes.toCompactFormat(),
    getCamera(), getLocationString(), urls.raw, urls.full, urls.regular, urls.small, urls.thumb,
    user.profileImage?.large, user.name, getTags()
)

fun GetPhotoResponse.getLocationString(): String = when {
    location.city != null && location.country != null ->
        "${location.city}, ${location.country}"
    location.city != null && location.country == null -> location.city!!
    location.city == null && location.country != null -> location.country!!
    else -> "N/A"
}

fun GetPhotoResponse.getCamera(): String {
    return exif.run {
        model?.apply {
            val makeList = make?.split(" ")?.map { it.trim().lowercase(Locale.ROOT) }
            val modelList = split(" ").map { it.trim().lowercase(Locale.ROOT) }

            if (makeList?.intersect(modelList)?.isEmpty() == true) {
                "${makeList.first()} $model"
            } else model
        }
    } ?: "Unknown"
}

fun GetPhotoResponse.getTags(): List<String?>? = tags?.map { it.title }

fun GetCollectionsResponse.toUnsplashCollection() = UnsplashCollection(
    id, title, description, user.name, user.username, user.profileImage?.large, isPrivate,
    coverPhoto?.color, coverPhoto?.urls?.thumb, coverPhoto?.urls?.regular, coverPhoto?.width,
    coverPhoto?.height, totalPhotos
)
