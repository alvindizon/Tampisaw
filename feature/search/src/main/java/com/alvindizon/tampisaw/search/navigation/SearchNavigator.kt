package com.alvindizon.tampisaw.search.navigation

import androidx.navigation.Navigator

interface SearchNavigator {

    fun navigateToCollection(
        id: String,
        description: String?,
        totalPhotos: Int,
        name: String?,
        title: String,
        extras: Navigator.Extras
    )
}
