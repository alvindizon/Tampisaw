package com.alvindizon.tampisaw.gallery

import androidx.paging.PagingData
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.gallery.model.PhotoUrls
import com.alvindizon.tampisaw.gallery.model.PhotoUser

internal object TestConstants {
    val photo1 = Photo(
        "id",
        "desc",
        PhotoUser("name", "username", "profileurl"),
        PhotoUrls("raw", "full", "reg", "small", "thumb"),
        false,
        "#E0E0E0",
        100,
        200
    )
    val photo2 = Photo(
        "id2",
        "desc",
        PhotoUser("name2", "username2", "profileurl2"),
        PhotoUrls("raw", "full", "reg", "small", "thumb"),
        false,
        "#E0E0E0",
        100,
        200
    )
    val getAllPhotosPagingData =
        PagingData.from(listOf(photo1, photo2))
}
