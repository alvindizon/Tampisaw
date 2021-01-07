package com.alvindizon.tampisaw.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentSearchCollectionListBinding
import com.alvindizon.tampisaw.ui.collections.CollectionAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchCollectionListFragment : BaseFragment(R.layout.fragment_search_collection_list) {

    private var binding: FragmentSearchCollectionListBinding? = null

    private val viewModel: SearchViewModel by activityViewModels()

    private lateinit var adapter: CollectionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchCollectionListBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        setupRetryButton()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        adapter = CollectionAdapter {
            findNavController().navigate(
                SearchFragmentDirections.collectionAction(
                    it.id, it.description, it.totalPhotos, it.fullname, it.title
                )
            )
        }

        viewModel.collections?.observe(viewLifecycleOwner, {
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
                // Only show the list if refresh succeeds.
                list.isVisible = loadState.source.refresh is LoadState.NotLoading
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
        fun newInstance() = SearchCollectionListFragment()
    }
}