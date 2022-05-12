package com.alvindizon.tampisaw.gallery.navigation

import androidx.navigation.Navigator

interface GalleryNavigator {
    fun navigateToDetails(
        name: String,
        photoId: String,
        regularUrl: String,
        profileImgUrl: String,
        extras: Navigator.Extras
    )
}
