package com.alvindizon.tampisaw

import androidx.paging.PagingData
import com.alvindizon.tampisaw.ui.collections.UnsplashCollection
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhotoUrls
import com.alvindizon.tampisaw.ui.gallery.UnsplashUser

internal object TestConstants {
    val unsplashUser = UnsplashUser(
        "name", "username", "profileImageUrl"
    )
    val unsplashPhotoUrls = UnsplashPhotoUrls(
        "raw", "full", "regular", "small", "thumb"
    )
    val unsplashPhoto = UnsplashPhoto(
        "id",
        "description",
        unsplashUser,
        unsplashPhotoUrls,
        false,
        "#FFFFFF",
        720,
        1250
    )
    val unsplashPhoto2 = UnsplashPhoto(
        "id2",
        "description2",
        unsplashUser,
        unsplashPhotoUrls,
        false,
        "#000000",
        720,
        1250
    )
    val unsplashPhotos = mutableListOf(unsplashPhoto, unsplashPhoto2)
    val photoPagingData = PagingData.from(unsplashPhotos)
    val unsplashCollection = UnsplashCollection(
        123,
        "collection",
        "wala lang",
        "eh",
    "ih",
        null,
        false,
        "#FFFFFF",
        "thumbUrl",
        "REGURL",
        720,
    1250,
        45
    )
    val unsplashCollection2 = UnsplashCollection(
        124,
        "collection2",
        "wala lang din",
        "oh",
        "uh",
        null,
        false,
        "#000000",
        "thumbUrl1",
        "REGURL1",
        720,
        1250,
        45
    )
    val unsplashCollections = mutableListOf(unsplashCollection, unsplashCollection2)
    val collectionsPagingData = PagingData.from(unsplashCollections)
}