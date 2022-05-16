package com.alvindizon.tampisaw.search.ui

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionListBinding
import com.alvindizon.tampisaw.collections.ui.BaseCollectionListFragment
import com.alvindizon.tampisaw.collections.ui.CollectionAdapter
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.search.databinding.FragmentSearchCollectionListBinding
import com.alvindizon.tampisaw.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchCollectionListFragment :
    BaseCollectionListFragment<FragmentSearchCollectionListBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()

    override fun FragmentSearchCollectionListBinding.provideCollectionBinding(): FragmentCollectionListBinding =
        searchCollectionContainer

    override fun fetchCollections() = Unit

    override fun observeViewModel(adapter: CollectionAdapter) {
        viewModel.collections.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override val viewBindingInflater: ViewBindingInflater<FragmentSearchCollectionListBinding> =
        FragmentSearchCollectionListBinding::inflate

    override fun onIsNotLoading(isNotLoading: Boolean) {
        val itemCount = binding.searchCollectionContainer.list.adapter?.itemCount
        itemCount?.let {
            // Show empty view if adapter itemCount is zero
            binding.emptyView.isVisible = it == 0 && isNotLoading
            // Only show the list if refresh succeeds and itemCount > 0
            binding.searchCollectionContainer.list.isVisible = it > 0 && isNotLoading
        }
    }

    companion object {
        fun newInstance() = SearchCollectionListFragment()
    }
}
