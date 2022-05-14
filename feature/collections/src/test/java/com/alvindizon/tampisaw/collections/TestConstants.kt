package com.alvindizon.tampisaw.collections

import androidx.paging.PagingData
import com.alvindizon.tampisaw.collections.model.Collection

internal object TestConstants {
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

    val getAllCollectionsPagingData = PagingData.from(listOf(collections, collections2))
}
