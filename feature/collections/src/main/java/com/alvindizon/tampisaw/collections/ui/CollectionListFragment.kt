package com.alvindizon.tampisaw.collections.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alvindizon.tampisaw.collections.R
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionListBinding
import com.alvindizon.tampisaw.collections.viewmodel.CollectionListViewModel
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.getNavigatorExtras
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionListFragment : Fragment(R.layout.fragment_collection_list) {

    private val viewModel: CollectionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all collections
        viewModel.getAllCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        FragmentCollectionListBinding.bind(view).apply {
            setupGallery(this)
        }
    }

    private fun setupGallery(binding: FragmentCollectionListBinding) {
        // Add a click listener for each list item
        val adapter = CollectionAdapter { collection, itemBinding ->
            val extras = getNavigatorExtras(itemBinding.collectionTitle)
            findNavController().navigate(
                R.id.collections_nav_graph,
                CollectionFragmentArgs(
                    collection.id,
                    collection.description,
                    collection.totalPhotos,
                    collection.fullname,
                    collection.title
                ).toBundle(),
                null,
                extras
            )
        }

        observeViewModel(binding, adapter)

        setupBinding(binding, adapter)
    }

    private fun observeViewModel(
        binding: FragmentCollectionListBinding,
        adapter: CollectionAdapter
    ) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setupBinding(binding: FragmentCollectionListBinding, adapter: CollectionAdapter) {
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

            swipeLayout.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    viewModel.getAllCollections()
                }
            }
        }
    }
}
