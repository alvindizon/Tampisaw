package com.alvindizon.tampisaw.details.model


data class PhotoDetails (
    val id: String,
    val createdAt: String?,
    val updatedAt: String?,
    val dimension: String?,
    val color: String? = "#E0E0E0",
    val views: String?,
    val downloads: String?,
    val likes: String?,
    val camera: String,
    val location: String,
    val rawUrl: String,
    val fullUrl: String,
    val regularUrl: String,
    val smallUrl: String,
    val thumbUrl: String,
    val profileImageUrl: String?,
    val username: String?,
    val tags: List<String?>?,
)


val PhotoDetails.fileName: String
    get() = "${this.id}.jpg"
