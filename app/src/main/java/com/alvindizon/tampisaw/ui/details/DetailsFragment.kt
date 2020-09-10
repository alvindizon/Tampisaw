package com.alvindizon.tampisaw.ui.details

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.WorkManager
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.hasWritePermission
import com.alvindizon.tampisaw.core.requestPermission
import com.alvindizon.tampisaw.core.ui.DialogManager
import com.alvindizon.tampisaw.core.utils.fileExists
import com.alvindizon.tampisaw.core.utils.showFileExistsDialog
import com.alvindizon.tampisaw.data.download.ImageDownloader
import com.alvindizon.tampisaw.databinding.FragmentDetailsBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class DetailsFragment: Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null

    private lateinit var viewModel: DetailsViewModel

    private val args: DetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var workManager: WorkManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState?.observe(this, {
            binding?.progressBar?.isVisible = it is LOADING
            binding?.image?.isVisible = it is SUCCESS
            if (it is SUCCESS) {
                binding?.image?.let { imgView ->
                    Glide.with(requireContext())
                        .load(it.photoDetails.regularUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imgView)

                    setupFabOptions(it.photoDetails)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.getPhoto(args.url)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupFabOptions(photoDetails: PhotoDetails) {
        binding?.apply {
            fab.forEach { item ->
                item.setOnClickListener {
                    when(it.id) {
                        R.id.faboption_1 -> dialogManager.showDialog(InfoBottomSheet.newInstance())
                        R.id.faboption_2 -> {
                            if(requireContext().fileExists(photoDetails.fileName)) {
                                showFileExistsDialog(requireContext()) {
                                    downloadPhoto(photoDetails)
                                }
                            } else {
                                downloadPhoto(photoDetails)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadPhoto(photoDetails: PhotoDetails) {
        if(requireContext().hasWritePermission()) {
            val request = ImageDownloader.enqueueDownload(
                photoDetails.regularUrl, photoDetails.fileName, photoDetails.id
            )
            workManager.enqueue(request)
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 0)
        }
    }
}
