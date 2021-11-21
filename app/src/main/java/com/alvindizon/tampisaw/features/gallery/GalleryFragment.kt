package com.alvindizon.tampisaw.features.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.databinding.FragmentGalleryBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private var binding: FragmentGalleryBinding? = null

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all photos, sorted by latest
        viewModel.getAllPhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGalleryBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        waitForTransition(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        val adapter = GalleryAdapter { photo, itemBinding ->
            photo.id.let {
                val extras = FragmentNavigatorExtras(itemBinding.username.toTransitionGroup())
                findNavController().navigate(
                    GalleryFragmentDirections.detailsAction(it, photo),
                    extras
                )
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            binding?.swipeLayout?.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setupBinding(adapter)

        setupRetryButton(adapter)
    }

    private fun setupBinding(adapter: GalleryAdapter) {
        binding?.apply {
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

            swipeLayout.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    viewModel.getAllPhotos()
                }
            }
        }

    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}
