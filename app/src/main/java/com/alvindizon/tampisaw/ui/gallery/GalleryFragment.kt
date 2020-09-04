package com.alvindizon.tampisaw.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.Const
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class GalleryFragment: Fragment(R.layout.fragment_gallery) {

    private var binding: FragmentGalleryBinding? = null

    private lateinit var viewModel: GalleryViewModel

    private lateinit var adapter: GalleryAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState?.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGalleryBinding.bind(view)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        setupRetryButton()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        adapter = GalleryAdapter{ photo ->
            photo.id.let {
                findNavController().navigate(GalleryFragmentDirections.detailsAction(it))
            }
        }

        // Apply the following settings to our recyclerview
        binding?.list?.run {
            setAdapter(adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter {
                    adapter.retry()
                },
                footer = RetryAdapter {
                    adapter.retry()
                }
            ))
            setLayoutManager(LinearLayoutManager(requireContext()))
            addVeiledItems(Const.PAGE_SIZE)
        }

        // Add a listener for the current state of paging
        adapter.addLoadStateListener { loadState ->
            // Unveil the list if refresh succeeds
            if(loadState.source.refresh is LoadState.NotLoading) {
                binding?.list?.unVeil()
            }
            // Shimmer during initial load or refresh
            if(loadState.source.refresh is LoadState.Loading) {
                binding?.list?.veil()
            }
            // Show the retry state if initial load or refresh fails.
            binding?.retryButton?.isVisible = loadState.source.refresh is LoadState.Error
            // On error, make the list invisible
            binding?.list?.isVisible = loadState.source.refresh !is LoadState.Error

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

        // display all photos, sorted by latest
        viewModel.getAllPhotos()
    }

    private fun setupRetryButton() {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}