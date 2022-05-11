package com.alvindizon.tampisaw.gallery.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val description: String?,
    val user: PhotoUser,
    val urls: PhotoUrls,
    val sponsored: Boolean,
    val color: String? = "#E0E0E0",
    val height: Int?,
    val width: Int?
) : Parcelable

@Parcelize
data class PhotoUrls(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?,
    val thumb: String?
) : Parcelable

@Parcelize
data class PhotoUser(
    val name: String,
    val username: String,
    val profileImageUrl: String?,
) : Parcelable
