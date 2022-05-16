package com.alvindizon.tampisaw.gallery.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.gallery.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.gallery.viewmodel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : BaseGalleryFragment<FragmentGalleryBinding, GalleryViewModel>() {

    override val viewModel: GalleryViewModel by viewModels()

    override fun FragmentGalleryBinding.provideGalleryBinding(): FragmentGalleryBinding = binding

    override val viewBindingInflater: ViewBindingInflater<FragmentGalleryBinding> =
        FragmentGalleryBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchPhotos()
    }

    override fun observeViewModel(adapter: GalleryAdapter) {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun fetchPhotos() {
        // display all photos, sorted by latest
        viewModel.getAllPhotos()
    }
}
