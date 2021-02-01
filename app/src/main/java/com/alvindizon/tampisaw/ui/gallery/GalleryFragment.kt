package com.alvindizon.tampisaw.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentGalleryBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery) {

    private var binding: FragmentGalleryBinding? = null

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // display all photos, sorted by latest
        viewModel.getAllPhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGalleryBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        binding?.run {
            val owner = this@GalleryFragment
            viewModel = owner.viewModel
            lifecycleOwner = owner
            val adapter = GalleryAdapter { photo ->
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
            list.adapter = adapter

            retryButton.setOnClickListener { adapter.retry() }

            owner.viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is GalleryUIState.DataLoaded -> {
                        adapter.submitData(lifecycle, uiState.pagingData)
                    }
                    is GalleryUIState.Error -> {
                        swipeLayout.isRefreshing = false
                        Snackbar.make(
                            requireView(),
                            uiState.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    GalleryUIState.GalleryVisible -> {
                        swipeLayout.isRefreshing = false
                        list.isVisible = true
                        progressBar.isVisible = false
                        retryButton.isVisible = false
                    }
                    GalleryUIState.Loading -> {
                        progressBar.isVisible = true
                        list.isVisible = false
                        retryButton.isVisible = false
                    }
                    GalleryUIState.Retry -> {
                        retryButton.isVisible = true
                        progressBar.isVisible = false
                        list.isVisible = false
                    }
                    GalleryUIState.Refresh -> {
                        swipeLayout.isRefreshing = true
                        owner.viewModel.getAllPhotos()
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}