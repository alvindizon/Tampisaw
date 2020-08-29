package com.alvindizon.tampisaw.core

import com.alvindizon.tampisaw.data.networking.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.data.networking.model.listphotos.Urls
import com.alvindizon.tampisaw.data.networking.model.listphotos.User
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhotoUrls
import com.alvindizon.tampisaw.ui.gallery.UnsplashUser

fun ListPhotosResponse.toUnsplashPhoto() = UnsplashPhoto(
    id, description, user.toUnsplashUser(), urls.toUnsplashUrls()
)

fun User.toUnsplashUser() = UnsplashUser(name, username)

fun Urls.toUnsplashUrls() = UnsplashPhotoUrls(raw, full, regular, small, thumb)