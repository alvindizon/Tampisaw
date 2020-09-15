package com.alvindizon.tampisaw.ui.gallery


data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val user: UnsplashUser,
    val urls: UnsplashPhotoUrls,
    val sponsored: Boolean,
    val color: String? = "#E0E0E0",
    val height: Int?,
    val width: Int?
)

data class UnsplashPhotoUrls(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?,
    val thumb: String?
)

data class UnsplashUser(
    val name: String,
    val username: String,
    val profileImageUrl: String?,
)
