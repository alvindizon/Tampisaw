package com.alvindizon.tampisaw.search.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alvindizon.tampisaw.core.utils.hideKeyboard
import com.alvindizon.tampisaw.search.R
import com.alvindizon.tampisaw.search.databinding.FragmentSearchBinding
import com.alvindizon.tampisaw.search.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null

    private val viewModel: SearchViewModel by activityViewModels()

    private val args: SearchFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // search for query if chip was clicked from details screen
        args.query?.let {
            viewModel.updateQuery(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding?.apply {
            viewPager.adapter = SearchAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = getString(SearchAdapter.SearchScreens.values()[position].titleRes)
            }.attach()

            searchText.run {
                args.query?.let { setText(it) }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.updateQuery(text.toString())
                        requireActivity().currentFocus?.hideKeyboard()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}

private class SearchAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    enum class SearchScreens(val titleRes: Int) {
        PHOTO(R.string.photos),
        COLLECTION(R.string.collections)
    }

    override fun getItemCount(): Int = SearchScreens.values().size

    override fun createFragment(position: Int): Fragment {
        return if (getItemViewType(position) == R.string.photos) {
            SearchPhotosFragment.newInstance()
        } else {
            return SearchCollectionListFragment.newInstance()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return SearchScreens.values()[position].titleRes
    }
}
