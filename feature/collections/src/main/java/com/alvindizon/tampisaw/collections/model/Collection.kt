package com.alvindizon.tampisaw.collections.model

data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    val fullname: String?,
    val username: String?,
    val profileImageUrl: String?,
    val private: Boolean?,
    val color: String? = "#E0E0E0",
    val coverPhotoThumbUrl: String?,
    val coverPhotoRegularUrl: String?,
    val coverPhotoWidth: Int?,
    val coverPhotoHeight: Int?,
    val totalPhotos: Int
)
