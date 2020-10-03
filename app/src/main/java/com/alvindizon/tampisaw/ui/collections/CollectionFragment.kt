package com.alvindizon.tampisaw.ui.collections

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.databinding.FragmentCollectionBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.alvindizon.tampisaw.ui.gallery.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CollectionFragment: Fragment(R.layout.fragment_collection) {

    private var binding: FragmentCollectionBinding? = null

    private lateinit var viewModel: CollectionViewModel

    private lateinit var adapter: GalleryAdapter

    private val args: CollectionFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CollectionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllPhotos(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCollectionBinding.bind(view)

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
                findNavController().navigate(CollectionFragmentDirections.detailsAction(it))
            }
        }

        viewModel.uiState?.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })

        binding?.apply {
            // Apply the following settings to our recyclerview
            collectionPhotoList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RetryAdapter {
                    adapter.retry()
                },
                footer = RetryAdapter {
                    adapter.retry()
                }
            )

            // Add a listener for the current state of paging
            adapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.
                collectionPhotoList.isVisible = loadState.source.refresh is LoadState.NotLoading
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

            upBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            args.description?.let {
                description.text = it
            }

            countCuratorView.text = getString(R.string.count_name, args.totalPhotos.toString(), args.name)
            toolbarTitle.text = args.title
        }
    }

    private fun setupRetryButton() {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}