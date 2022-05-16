package com.alvindizon.tampisaw.collections.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.alvindizon.tampisaw.collections.R
import com.alvindizon.tampisaw.collections.databinding.FragmentCollectionBinding
import com.alvindizon.tampisaw.collections.viewmodel.CollectionViewModel
import com.alvindizon.tampisaw.core.utils.ViewBindingInflater
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.gallery.databinding.FragmentGalleryBinding
import com.alvindizon.tampisaw.gallery.ui.BaseGalleryFragment
import com.alvindizon.tampisaw.gallery.ui.GalleryAdapter
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionFragment : BaseGalleryFragment<FragmentCollectionBinding, CollectionViewModel>() {

    private val args: CollectionFragmentArgs by navArgs()

    override val viewModel: CollectionViewModel by viewModels()

    override fun FragmentCollectionBinding.provideGalleryBinding(): FragmentGalleryBinding =
        collectionPhotoListContainer

    override fun observeViewModel(adapter: GalleryAdapter) {
        viewModel.uiState.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override val viewBindingInflater: ViewBindingInflater<FragmentCollectionBinding> =
        FragmentCollectionBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            removeTarget(R.id.collection_title)
        }

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun fetchPhotos() {
        viewModel.getAllPhotos(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.transitionName =
            getString(R.string.transition_collection_title, args.id)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        setupGallery()

        with(binding) {
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

        waitForTransition(view)
    }
}
