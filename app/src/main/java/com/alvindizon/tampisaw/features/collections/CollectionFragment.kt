package com.alvindizon.tampisaw.features.collections

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingDataAdapter
import androidx.transition.TransitionInflater
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.databinding.FragmentCollectionBinding
import com.alvindizon.tampisaw.features.gallery.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private var binding: FragmentCollectionBinding? = null

    private val viewModel: CollectionViewModel by viewModels()

    private val args: CollectionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            removeTarget(R.id.collection_title)
        }

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        viewModel.getAllPhotos(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCollectionBinding.bind(view)
        binding?.id = args.id
        binding?.executePendingBindings()

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        waitForTransition(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupGallery() {
        // Add a click listener for each list item
        val adapter = GalleryAdapter { photo, itemBinding ->
            photo.id.let {
                val extras = FragmentNavigatorExtras(itemBinding.username.toTransitionGroup())
                findNavController().navigate(
                    CollectionFragmentDirections.detailsAction(it, photo),
                    extras
                )
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
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
            adapter.setLoadStateListener(
                // Only show the list if refresh succeeds.
                isNotLoading = { collectionPhotoList.isVisible = it },
                // Show loading spinner during initial load or refresh.
                isLoading = { progressBar.isVisible = it },
                // Show the retry state if initial load or refresh fails.
                isLoadStateError = { retryButton.isVisible = it },
                errorListener = {
                    Snackbar.make(
                        requireView(),
                        "\uD83D\uDE28 Wooops $it",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )

            upBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            if (args.description != null) {
                description.isVisible = true
                description.text = args.description
            }

            countCuratorView.text =
                getString(R.string.count_name, args.totalPhotos.toString(), args.name)
            toolbarTitle.text = args.title
        }

        setupRetryButton(adapter)
    }

    private fun setupRetryButton(adapter: PagingDataAdapter<*, *>) {
        binding?.retryButton?.setOnClickListener {
            adapter.retry()
        }
    }
}
