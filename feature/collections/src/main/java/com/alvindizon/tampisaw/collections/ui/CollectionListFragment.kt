package com.alvindizon.tampisaw.collections.ui

import androidx.fragment.app.viewModels
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionListBinding
import com.alvindizon.tampisaw.collections.viewmodel.CollectionListViewModel
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionListFragment :
    BaseCollectionListFragment<FragmentCollectionListBinding, CollectionListViewModel>() {

    override val viewModel: CollectionListViewModel by viewModels()

    override fun FragmentCollectionListBinding.provideCollectionBinding(): FragmentCollectionListBinding =
        binding

    override val viewBindingInflater: ViewBindingInflater<FragmentCollectionListBinding> =
        FragmentCollectionListBinding::inflate

    override fun observeViewModel(adapter: CollectionAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun fetchCollections() {
        // display all collections
        viewModel.getAllCollections()
    }
}
