package com.alvindizon.tampisaw.navigation

import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.details.ui.DetailsFragmentArgs
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GalleryNavigatorImpl @Inject constructor(
    private val navController: NavController
) : GalleryNavigator {

    override fun navigateToDetails(
        name: String,
        photoId: String,
        regularUrl: String,
        profileImgUrl: String,
        extras: Navigator.Extras
    ) {
        navController.navigate(
            R.id.details_nav_graph,
            DetailsFragmentArgs(name, photoId, regularUrl, profileImgUrl).toBundle(),
            null,
            extras
        )
    }
}
