package com.alvindizon.tampisaw.features.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentSearchPhotosBinding
import com.alvindizon.tampisaw.features.gallery.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchPhotosFragment : BaseFragment(R.layout.fragment_search_photos) {

    private var binding: FragmentSearchPhotosBinding? = null

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var adapter: GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchPhotosBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        setupRetryButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        adapter = GalleryAdapter { photo ->
            photo.id.let {
                // TODO create activity for displaying photo details
                findNavController().navigate(SearchFragmentDirections.detailsAction(it))
            }
        }

        viewModel.photos.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })

        binding?.apply {
            // Apply the following settings to our recyclerview
            list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter {
                    adapter.retry()
                },
                footer = RetryAdapter {
                    adapter.retry()
                }
            )

            // Add a listener for the current state of paging
            adapter.addLoadStateListener { loadState ->
                // Show empty view if adapter itemCount is zero
                emptyView.isVisible =
                    adapter.itemCount == 0 && loadState.source.refresh !is LoadState.Loading && loadState.source.refresh !is LoadState.Error
                // Only show the list if refresh succeeds and itemCount > 0
                list.isVisible =
                    loadState.source.refresh is LoadState.NotLoading && adapter.itemCount > 0
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(
                        requireView(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }

    companion object {
        fun newInstance() = SearchPhotosFragment()
    }
}
