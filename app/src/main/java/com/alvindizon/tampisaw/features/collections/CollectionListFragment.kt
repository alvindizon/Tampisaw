package com.alvindizon.tampisaw.features.collections

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.getNavigatorExtras
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.databinding.FragmentCollectionListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CollectionListFragment : Fragment(R.layout.fragment_collection_list) {

    private var binding: FragmentCollectionListBinding? = null

    private val viewModel: CollectionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all collections
        viewModel.getAllCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitForTransition(view)

        binding = FragmentCollectionListBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        val adapter = CollectionAdapter { collection, itemBinding ->
            val extras = getNavigatorExtras(itemBinding.collectionTitle)
            findNavController().navigate(
                CollectionListFragmentDirections.collectionAction(
                    collection.id,
                    collection.description,
                    collection.totalPhotos,
                    collection.fullname,
                    collection.title
                ),
                extras
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            binding?.swipeLayout?.isRefreshing = false
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
                    viewModel.getAllCollections()
                }
            }
        }

        setupRetryButton(adapter)
    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}
