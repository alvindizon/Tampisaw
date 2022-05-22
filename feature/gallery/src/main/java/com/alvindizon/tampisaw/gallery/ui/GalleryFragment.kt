package com.alvindizon.tampisaw.gallery.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.gallery.R
import com.alvindizon.tampisaw.gallery.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.alvindizon.tampisaw.gallery.viewmodel.GalleryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    @Inject
    lateinit var galleryNavigator: GalleryNavigator

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all photos, sorted by latest
        viewModel.getAllPhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        FragmentGalleryBinding.bind(view).apply {
            setupGallery(this)
        }

        waitForTransition(view)
    }

    private fun setupGallery(binding: FragmentGalleryBinding) {
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

        observeViewModel(binding, adapter)

        setupBinding(binding, adapter)
    }

    private fun observeViewModel(binding: FragmentGalleryBinding, adapter: GalleryAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setupBinding(binding: FragmentGalleryBinding, adapter: GalleryAdapter) {
        with(binding) {
            // Apply the following settings to our recyclerview
            list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter { adapter.retry() },
                footer = RetryAdapter { adapter.retry() }
            )

            // Add a listener for the current state of paging
            adapter.setLoadStateListener(
                // Only show the list if refresh succeeds.
                isNotLoading = { list.isVisible = it },
                // Show loading spinner during initial load or refresh.
                isLoading = { progressBar.isVisible = it && !swipeLayout.isRefreshing },
                // Show the retry state if initial load or refresh fails.
                isLoadStateError = { retryButton.isVisible = it },
                errorListener = {
                    swipeLayout.isRefreshing = false
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
}
