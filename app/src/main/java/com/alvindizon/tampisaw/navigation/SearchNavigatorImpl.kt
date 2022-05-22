package com.alvindizon.tampisaw.navigation

import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.collections.ui.CollectionFragmentArgs
import com.alvindizon.tampisaw.search.navigation.SearchNavigator
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SearchNavigatorImpl @Inject constructor(
    private val navController: NavController
) : SearchNavigator {

    override fun navigateToCollection(
        id: String,
        description: String?,
        totalPhotos: Int,
        name: String?,
        title: String,
        extras: Navigator.Extras
    ) {
        navController.navigate(
            R.id.collections_nav_graph,
            CollectionFragmentArgs(id, description, totalPhotos, name, title).toBundle(),
            null,
            extras
        )
    }
}
