package com.alvindizon.tampisaw.navigation

import androidx.navigation.NavController
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.details.navigation.DetailsNavigator
import com.alvindizon.tampisaw.search.ui.SearchFragmentArgs
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DetailsNavigatorImpl @Inject constructor(
    private val navController: NavController
): DetailsNavigator {

    override fun navigateToSearch(clickedTag: String) {
        navController.navigate(
            R.id.search_nav_graph,
            SearchFragmentArgs(clickedTag).toBundle()
        )
    }
}
