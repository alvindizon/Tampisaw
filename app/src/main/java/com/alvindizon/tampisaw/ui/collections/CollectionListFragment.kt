package com.alvindizon.tampisaw.ui.collections

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentCollectionListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CollectionListFragment : BaseFragment(R.layout.fragment_collection_list) {

    private var binding: FragmentCollectionListBinding? = null

    private val viewModel: CollectionListViewModel by viewModels()

    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all collections
        viewModel.getAllCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCollectionListBinding.bind(view)

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
                CollectionListFragmentDirections.collectionAction(
                    it.id,
                    it.description,
                    it.totalPhotos,
                    it.fullname,
                    it.title
                )
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner, {
            binding?.swipeLayout?.isRefreshing = false
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
                progressBar.isVisible =
                    loadState.source.refresh is LoadState.Loading && !swipeLayout.isRefreshing
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    swipeLayout.isRefreshing = false
                    Snackbar.make(
                        requireView(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            swipeLayout.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    viewModel.getAllCollections()
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}