package com.alvindizon.tampisaw.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentSearchPhotosBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.alvindizon.tampisaw.ui.gallery.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SearchPhotosFragment: Fragment(R.layout.fragment_search_photos) {

    private var binding: FragmentSearchPhotosBinding? = null

    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: GalleryAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchPhotosBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        setupRetryButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        adapter = GalleryAdapter{ photo ->
            photo.id.let {
                // TODO create activity for displaying photo details
                findNavController().navigate(SearchFragmentDirections.detailsAction(it))
            }
        }

        viewModel.photos?.observe(viewLifecycleOwner, {
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
                Log.d("GalleryFragment", "LoadState: " + loadState.source.refresh.toString())
                // Only show the list if refresh succeeds.
                list.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(requireView(),
                            "\uD83D\uDE28 Wooops ${it.error}",
                            Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }

    companion object {
        fun newInstance() = SearchPhotosFragment()
    }
}