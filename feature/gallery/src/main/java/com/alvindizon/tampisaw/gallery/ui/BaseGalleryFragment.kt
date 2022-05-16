package com.alvindizon.tampisaw.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.gallery.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


abstract class BaseGalleryFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    @Inject
    lateinit var galleryNavigator: GalleryNavigator

    private var _galleryBinding: FragmentGalleryBinding? = null
    private val galleryBinding: FragmentGalleryBinding get() = _galleryBinding!!

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    abstract fun VB.provideGalleryBinding(): FragmentGalleryBinding

    protected abstract fun fetchPhotos()

    protected abstract fun observeViewModel(adapter: GalleryAdapter)

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

        fetchPhotos()
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

    protected open fun setupGallery() {
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

        observeViewModel(adapter)

        setupBinding(adapter)

        setupRetryButton(adapter)
    }

    protected open fun setupBinding(adapter: GalleryAdapter) {
        _galleryBinding?.apply {
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
                    fetchPhotos()
                }
            }
        }
    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        _galleryBinding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }

    protected open fun onIsNotLoading(isNotLoading: Boolean) {
        galleryBinding.list.isVisible = isNotLoading
    }

    protected open fun onLoading(isLoading: Boolean) {
        galleryBinding.progressBar.isVisible = isLoading && !galleryBinding.swipeLayout.isRefreshing
    }

    protected open fun onIsLoadStateError(isLoadStateError: Boolean) {
        galleryBinding.retryButton.isVisible = isLoadStateError
    }

    protected open fun onAdapterError(throwable: Throwable) {
        galleryBinding.swipeLayout.isRefreshing = false
        Snackbar.make(
            requireView(),
            "\uD83D\uDE28 Wooops $throwable",
            Snackbar.LENGTH_LONG
        ).show()
    }
}
