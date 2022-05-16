package com.alvindizon.tampisaw.search

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.model.Collection
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
    val listPhotosPagingData =
        PagingData.from(listOf(photo1, photo2))
    val collections = Collection(
        "123",
        "collection",
        "wala lang",
        "name",
        "username",
        "profileimageUrllarge",
        false,
        "#FFFFFF",
        "thumbUrl",
        "regularUrl",
        720,
        1250,
        100
    )

    val collections2 = Collection(
        "123",
        "collection2",
        "wala lang 2",
        "name",
        "username",
        "profileimageUrllarge2",
        false,
        "#FFFFFF",
        "thumbUrl2",
        "regularUrl2",
        720,
        1250,
        100
    )
    val collectionsResponsePagingData = PagingData.from(listOf(collections, collections2))
}
