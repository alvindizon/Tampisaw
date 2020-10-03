package com.alvindizon.tampisaw.ui.collections

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentCollectionListBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CollectionListFragment: Fragment(R.layout.fragment_collection_list) {

    private var binding: FragmentCollectionListBinding? = null

    private lateinit var viewModel: CollectionListViewModel

    private lateinit var adapter: CollectionAdapter

    @Inject lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CollectionListViewModel::class.java)
    }

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
        adapter = CollectionAdapter{ collection ->
            collection.title.let {
                Log.d("CollectionsFragment", it)
            }
        }

        viewModel.uiState?.observe(viewLifecycleOwner, {
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
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading && !swipeLayout.isRefreshing
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    swipeLayout.isRefreshing = false
                    Snackbar.make(requireView(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Snackbar.LENGTH_LONG).show()
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