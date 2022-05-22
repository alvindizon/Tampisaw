package com.alvindizon.tampisaw.search.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.alvindizon.tampisaw.gallery.ui.GalleryAdapter
import com.alvindizon.tampisaw.search.R
import com.alvindizon.tampisaw.search.databinding.FragmentSearchPhotosBinding
import com.alvindizon.tampisaw.search.viewmodel.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchPhotosFragment : Fragment(R.layout.fragment_search_photos) {

    @Inject
    lateinit var galleryNavigator: GalleryNavigator

    // TODO use navgraph scoped viewmodels
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        FragmentSearchPhotosBinding.bind(view).apply {
            setupGallery(this)
        }
    }

    private fun setupGallery(binding: FragmentSearchPhotosBinding) {
        // Add a click listener for each list item
        val adapter = GalleryAdapter { photo, itemBinding ->
            photo.id.let {
                val extras = FragmentNavigatorExtras(itemBinding.username.toTransitionGroup())
                galleryNavigator.navigateToDetails(
                    photo.user.name,
                    it,
                    photo.urls.regular ?: "",
                    photo.user.profileImageUrl ?: "",
                    extras
                )
            }
        }

        observeViewModel(adapter)

        setupBinding(binding, adapter)
    }

    private fun observeViewModel(adapter: GalleryAdapter) {
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setupBinding(binding: FragmentSearchPhotosBinding, adapter: GalleryAdapter) {
        with(binding) {
            // Apply the following settings to our recyclerview
            list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter { adapter.retry() },
                footer = RetryAdapter { adapter.retry() }
            )

            // Add a listener for the current state of paging
            adapter.setLoadStateListener(
                isNotLoading = {
                    // Show empty view if adapter itemCount is zero
                    emptyView.isVisible = adapter.itemCount == 0 && it
                    // Only show the list if refresh succeeds and itemCount > 0
                    list.isVisible = adapter.itemCount > 0 && it
                },
                // Show loading spinner during initial load or refresh.
                isLoading = { progressBar.isVisible = it },
                // Show the retry state if initial load or refresh fails.
                isLoadStateError = { retryButton.isVisible = it },
                errorListener = {
                    Snackbar.make(
                        requireView(),
                        "\uD83D\uDE28 Wooops $it",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )

            retryButton.setOnClickListener { adapter.retry() }
        }
    }


    companion object {
        fun newInstance() = SearchPhotosFragment()
    }
}
