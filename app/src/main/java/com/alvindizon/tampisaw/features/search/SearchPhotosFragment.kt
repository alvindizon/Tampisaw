package com.alvindizon.tampisaw.features.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.databinding.FragmentSearchPhotosBinding
import com.alvindizon.tampisaw.features.gallery.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchPhotosFragment : Fragment(R.layout.fragment_search_photos) {

    private var binding: FragmentSearchPhotosBinding? = null

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchPhotosBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        postponeEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        val adapter = GalleryAdapter(
            onImageLoadFailed = { startPostponedEnterTransition() },
            onImageResourceReady = { startPostponedEnterTransition() },
            listener = { photo, itemBinding ->
                photo.id.let {
                    val extras = FragmentNavigatorExtras(
                        itemBinding.avatar.toTransitionGroup(),
                        itemBinding.imageView.toTransitionGroup(),
                        itemBinding.username.toTransitionGroup(),
                        itemBinding.handle.toTransitionGroup()
                    )
                    findNavController().navigate(
                        SearchFragmentDirections.detailsAction(it, photo, true),
                        extras
                    )
                }
            })

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        binding?.apply {
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
        }

        setupRetryButton(adapter)
    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }

    companion object {
        fun newInstance() = SearchPhotosFragment()
    }
}
