package com.alvindizon.tampisaw.search.ui

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.gallery.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.gallery.ui.BaseGalleryFragment
import com.alvindizon.tampisaw.gallery.ui.GalleryAdapter
import com.alvindizon.tampisaw.search.databinding.FragmentSearchPhotosBinding
import com.alvindizon.tampisaw.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchPhotosFragment : BaseGalleryFragment<FragmentSearchPhotosBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()

    override fun FragmentSearchPhotosBinding.provideGalleryBinding(): FragmentGalleryBinding =
        searchPhotosContainer

    override fun fetchPhotos() = Unit

    override fun observeViewModel(adapter: GalleryAdapter) {
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override val viewBindingInflater: ViewBindingInflater<FragmentSearchPhotosBinding> =
        FragmentSearchPhotosBinding::inflate

    override fun onIsNotLoading(isNotLoading: Boolean) {
        val itemCount = binding.searchPhotosContainer.list.adapter?.itemCount
        itemCount?.let {
            // Show empty view if adapter itemCount is zero
            binding.emptyView.isVisible = itemCount == 0 && isNotLoading
            // Only show the list if refresh succeeds and itemCount > 0
            binding.searchPhotosContainer.list.isVisible = itemCount > 0 && isNotLoading
        }
    }

    companion object {
        fun newInstance() = SearchPhotosFragment()
    }
}
