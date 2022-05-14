package com.alvindizon.tampisaw

import androidx.paging.PagingData
import com.alvindizon.tampisaw.api.model.getcollections.CoverPhoto
import com.alvindizon.tampisaw.api.model.getcollections.CoverPhotoUrl
import com.alvindizon.tampisaw.api.model.getcollections.GetCollectionsResponse
import com.alvindizon.tampisaw.api.model.getcollections.User
import com.alvindizon.tampisaw.api.model.listphotos.ListPhotosResponse
import com.alvindizon.tampisaw.api.model.listphotos.ProfileImage
import com.alvindizon.tampisaw.api.model.listphotos.Urls
import com.alvindizon.tampisaw.core.toCollection
import com.alvindizon.tampisaw.core.toUnsplashPhoto
import com.alvindizon.tampisaw.data.download.ImageDownloader

internal object TestConstants {

    val listPhotosResponse = ListPhotosResponse(
        "#FFFFFF", null, "desc",
        1250, "id", false, 1,
        Urls("raw", "full", "regular", "small", "thumb"),
        com.alvindizon.tampisaw.api.model.listphotos.User(
            "id",
            "location",
            "name", ProfileImage("profileImageUrl"), "username"
        ),
        720
    )
    val listPhotosResponse2 = ListPhotosResponse(
        "#000000", null, "desc2",
        1250, "id2", false, 1,
        Urls("raw", "full", "regular", "small", "thumb"),
        com.alvindizon.tampisaw.api.model.listphotos.User(
            "id",
            "location",
            "name", ProfileImage("profileImageUrl"), "username"
        ),
        720
    )
    val unsplashPhoto = listPhotosResponse.toUnsplashPhoto()
    val unsplashPhoto2 = listPhotosResponse2.toUnsplashPhoto()
    private val unsplashPhotos = mutableListOf(unsplashPhoto, unsplashPhoto2)
    val photoPagingData = PagingData.from(unsplashPhotos)
    val listPhotosPagingData =
        PagingData.from(mutableListOf(listPhotosResponse, listPhotosResponse2))

    private val collectionsResponse = GetCollectionsResponse(
        "123",
        "collection",
        "wala lang",
        45,
        false,
        User(
            "id", "eh", "ih", null,
        ),
        CoverPhoto(
            "id", 720,
            1250, "#FFFFFF", CoverPhotoUrl("REGURL", "thumbUrl")
        )
    )

    private val collectionsResponse2 = GetCollectionsResponse(
        "124",
        "collection2",
        "wala lang din",
        45,
        false,
        User(
            "id", "oh", "uh", null,
        ),
        CoverPhoto(
            "id", 720,
            1250, "#000000", CoverPhotoUrl("REGURL1", "thumbUrl1")
        )
    )

    val unsplashCollection = collectionsResponse.toCollection()
    val unsplashCollection2 = collectionsResponse2.toCollection()
    val unsplashCollections = mutableListOf(unsplashCollection, unsplashCollection2)
    val collectionsPagingData = PagingData.from(unsplashCollections)
    val collectionsResponsePagingData =
        PagingData.from(mutableListOf(collectionsResponse, collectionsResponse2))

    // mocking workdata results in error(SignedCall not matching), need to create input data to match call
    val inputData = ImageDownloader.createInputData(QUALITY, FILENAME, ID)

}
