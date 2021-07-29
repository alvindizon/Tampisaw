package com.alvindizon.tampisaw.core

import com.alvindizon.tampisaw.data.networking.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.data.networking.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.Urls
import com.alvindizon.tampisaw.data.networking.model.listphotos.User
import com.alvindizon.tampisaw.features.collections.UnsplashCollection
import com.alvindizon.tampisaw.features.details.PhotoDetails
import com.alvindizon.tampisaw.features.gallery.UnsplashPhoto
import com.alvindizon.tampisaw.features.gallery.UnsplashPhotoUrls
import com.alvindizon.tampisaw.features.gallery.UnsplashUser
import java.util.*

fun ListPhotosResponse.toUnsplashPhoto() = UnsplashPhoto(
    id, description, user.toUnsplashUser(), urls.toUnsplashUrls(), sponsorship != null, color,
    height, width
)

fun User.toUnsplashUser() = UnsplashUser(name, username, profileImage?.large)

fun Urls.toUnsplashUrls() = UnsplashPhotoUrls(raw, full, regular, small, thumb)

fun GetPhotoResponse.toPhotoDetails() = PhotoDetails(
    id, createdAt, updatedAt, "$width x $height", color, views.toCompactFormat(),
    downloads.toCompactFormat(), likes.toCompactFormat(), description,
    getCamera(), getLocationString(), links.download, links.downloadLocation,
    urls.raw, urls.full, urls.regular, urls.small, urls.thumb, user.profileImage?.large, user.name,
    getTags()
)

fun GetPhotoResponse.getLocationString(): String = when {
    location.city != null && location.country != null ->
        "${location.city}, ${location.country}"
    location.city != null && location.country == null -> location.city
    location.city == null && location.country != null -> location.country
    else -> "N/A"
}

fun GetPhotoResponse.getCamera(): String {
    return exif.run {
        model?.apply{
            val makeList = make?.split(" ")?.map { it.trim().lowercase(Locale.ROOT) }
            val modelList = model.split(" ").map { it.trim().lowercase(Locale.ROOT) }

            if(makeList?.intersect(modelList)?.isEmpty() == true) {
                "${makeList.first()} $model"
            } else model
        }
    } ?: "Unknown"
}

fun GetPhotoResponse.getTags(): List<String?>?  = tags?.map { it.title }

fun GetCollectionsResponse.toUnsplashCollection() = UnsplashCollection(
    id, title, description, user.name, user.username, user.profileImage?.large, `private`,
    coverPhoto?.color, coverPhoto?.urls?.thumb, coverPhoto?.urls?.regular, coverPhoto?.width,
    coverPhoto?.height, totalPhotos
)
