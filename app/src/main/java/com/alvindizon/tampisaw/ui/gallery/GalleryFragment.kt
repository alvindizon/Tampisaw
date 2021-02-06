package com.alvindizon.tampisaw.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentGalleryBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery), GalleryView {

    private var binding: FragmentGalleryBinding? = null

    private val viewModel: GalleryViewModel by viewModels()

    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all photos, sorted by latest
        viewModel.getAllPhotos()
        viewModel.uiState.observe(this, this::render)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGalleryBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupAdapter()

        binding?.run {
            val owner = this@GalleryFragment
            viewModel = owner.viewModel
            lifecycleOwner = owner
            list.adapter = adapter
            retryButton.setOnClickListener { adapter.retry() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupAdapter() {
        adapter = GalleryAdapter { photo ->
            photo.id.let {
                findNavController().navigate(GalleryFragmentDirections.detailsAction(it))
            }
        }
        adapter.withLoadStateHeaderAndFooter(
            header = RetryAdapter {
                adapter.retry()
            },
            footer = RetryAdapter {
                adapter.retry()
            }
        )
    }

    override fun dataLoaded(pagingData: PagingData<UnsplashPhoto>) =
        adapter.submitData(lifecycle, pagingData)

    override fun showList(isListVisible: Boolean) {
        binding?.list?.isVisible = isListVisible
    }

    override fun setSwipeLayoutState(isRefreshing: Boolean) {
        binding?.swipeLayout?.isRefreshing = isRefreshing
    }

    override fun showProgressBar(isProgressBarVisible: Boolean) {
        binding?.progressBar?.isVisible = isProgressBarVisible
    }

    override fun showRetryButton(isRetryBtnVisible: Boolean) {
        binding?.retryButton?.isVisible = isRetryBtnVisible
    }

    override fun showErrorMessage(message: String) =
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()

    override fun loadPhotos() = viewModel.getAllPhotos()
}
