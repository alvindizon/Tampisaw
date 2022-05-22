package com.alvindizon.tampisaw.collections.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.alvindizon.tampisaw.collections.R
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionBinding
import com.alvindizon.tampisaw.collections.viewmodel.CollectionViewModel
import com.alvindizon.tampisaw.core.ui.RetryAdapter
import com.alvindizon.tampisaw.core.utils.setLoadStateListener
import com.alvindizon.tampisaw.core.utils.toTransitionGroup
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.gallery.navigation.GalleryNavigator
import com.alvindizon.tampisaw.gallery.ui.GalleryAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    @Inject
    lateinit var galleryNavigator: GalleryNavigator

    private val args: CollectionFragmentArgs by navArgs()

    private val viewModel: CollectionViewModel by viewModels()

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

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        FragmentCollectionBinding.bind(view).apply {
            setupGallery(this)
        }

        waitForTransition(view)
    }

    private fun setupGallery(binding: FragmentCollectionBinding) {
        // Add a click listener for each list item
        val adapter = GalleryAdapter { photo, itemBinding ->
            photo.id.let {
                val extras = FragmentNavigatorExtras(itemBinding.username.toTransitionGroup())
                galleryNavigator.navigateToDetails(
                    photo.user.name,
                    it,
                    photo.urls.regular ?: "",
                    photo.user.profileImageUrl ?: "",
                    extras
                )
            }
        }

        observeViewModel(adapter)

        setupBinding(binding, adapter)
    }

    private fun setupBinding(binding: FragmentCollectionBinding, adapter: GalleryAdapter) {
        with(binding) {
            toolbarTitle.transitionName =
                getString(R.string.transition_collection_title, args.id)

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

            args.description?.let {
                description.isVisible = true
                description.text = it
            }

            countCuratorView.text =
                getString(R.string.count_name, args.totalPhotos.toString(), args.name)
            toolbarTitle.text = args.title
        }
    }

    private fun observeViewModel(adapter: GalleryAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
}
