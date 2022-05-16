package com.alvindizon.tampisaw.collections.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionListBinding
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.core.utils.getNavigatorExtras
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.google.android.material.snackbar.Snackbar


abstract class BaseCollectionListFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _collectionListBinding: FragmentCollectionListBinding? = null
    private val collectionListBinding: FragmentCollectionListBinding get() = _collectionListBinding!!

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    abstract fun VB.provideCollectionBinding(): FragmentCollectionListBinding

    protected abstract fun fetchCollections()

    protected abstract fun observeViewModel(adapter: CollectionAdapter)

    protected abstract val viewBindingInflater: ViewBindingInflater<VB>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = viewBindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGallery()

        waitForTransition(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        observeViewModel(adapter)

        _collectionListBinding?.apply {
            // Apply the following settings to our recyclerview
            list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter { adapter.retry() },
                footer = RetryAdapter { adapter.retry() }
            )

            // Add a listener for the current state of paging
            adapter.setLoadStateListener(
                // Only show the list if refresh succeeds.
                isNotLoading = { onIsNotLoading(it) },
                // Show loading spinner during initial load or refresh.
                isLoading = { onLoading(it) },
                // Show the retry state if initial load or refresh fails.
                isLoadStateError = { onIsLoadStateError(it) },
                errorListener = { onAdapterError(it) }
            )

            swipeLayout.apply {
                setOnRefreshListener {
                    isRefreshing = true
                    fetchCollections()
                }
            }
        }

        setupRetryButton(adapter)
    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        _collectionListBinding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }

    protected open fun onIsNotLoading(isNotLoading: Boolean) {
        collectionListBinding.list.isVisible = isNotLoading
    }

    protected open fun onLoading(isLoading: Boolean) {
        collectionListBinding.progressBar.isVisible =
            isLoading && !collectionListBinding.swipeLayout.isRefreshing
    }

    protected open fun onIsLoadStateError(isLoadStateError: Boolean) {
        collectionListBinding.retryButton.isVisible = isLoadStateError
    }

    protected open fun onAdapterError(throwable: Throwable) {
        collectionListBinding.swipeLayout.isRefreshing = false
        Snackbar.make(
            requireView(),
            "\uD83D\uDE28 Wooops $throwable",
            Snackbar.LENGTH_LONG
        ).show()
    }
}
