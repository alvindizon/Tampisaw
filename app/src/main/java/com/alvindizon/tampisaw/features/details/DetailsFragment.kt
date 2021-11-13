package com.alvindizon.tampisaw.features.details

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.hasWritePermission
import com.alvindizon.tampisaw.core.utils.dismissCurrentDialog
import com.alvindizon.tampisaw.core.utils.fileExists
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.core.utils.showDialogFragment
import com.alvindizon.tampisaw.core.utils.showFileExistsDialog
import com.alvindizon.tampisaw.core.utils.showSnackbar
import com.alvindizon.tampisaw.core.utils.waitForTransition
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import com.alvindizon.tampisaw.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null

    private val viewModel: DetailsViewModel by activityViewModels()

    private val args: DetailsFragmentArgs by navArgs()

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // do nothing
        }

    private var snackbar: Snackbar? = null

    @Inject
    lateinit var activityFragmentManager: FragmentManager

    @Inject
    lateinit var wallpaperSettingManager: WallpaperSettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState.observe(this) { uiState ->
            binding?.run {
                progressBar.isVisible = uiState is Loading
                image.isVisible = uiState !is Loading
                handleUiState(uiState, this)
            }
        }

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private fun handleUiState(uiState: DetailsUIState?, binding: FragmentDetailsBinding) {
        with(binding) {
            when (uiState) {
                is GetDetailSuccess -> {
                    // TODO just pass URL from gallery, only load details when viewing info
                    image.let { imgView ->
                        Glide.with(requireContext())
                            .load(uiState.photoDetails.regularUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imgView)
                        setupFabOptions(uiState.photoDetails)
                        setupToolbar(uiState.photoDetails)
                    }
                }
                is Error -> {
                    snackbar = root.rootView.showSnackbar(
                        uiState.message,
                        Snackbar.LENGTH_SHORT,
                        anchorView = fab
                    )
                }
                DownloadSuccess -> {
                    snackbar = root.rootView.showSnackbar(
                        getString(R.string.download_complete),
                        Snackbar.LENGTH_SHORT,
                        null,
                        null,
                        fab
                    )
                }
                SettingWallpaper -> {
                    snackbar = root.rootView.showSnackbar(
                        getString(R.string.wallpaper_setting_ongoing),
                        Snackbar.LENGTH_INDEFINITE,
                        null,
                        null,
                        fab
                    )
                }
                SetWallpaperSuccess -> {
                    snackbar = root.rootView.showSnackbar(
                        getString(R.string.wallpaper_setting_success),
                        Snackbar.LENGTH_SHORT,
                        null,
                        null,
                        fab
                    )
                }
                else -> Unit
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        waitForTransition(view)

        binding = FragmentDetailsBinding.bind(view)
        binding?.photo = args.photo
        binding?.executePendingBindings()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.getPhoto(args.url)
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        binding = null
        super.onDestroyView()
    }

    private fun setupFabOptions(photoDetails: PhotoDetails) {
        binding?.apply {
            fabLayout.forEach { item ->
                item.setOnClickListener {
                    if (it.id == R.id.download_option) {
                        requireActivity().run {
                            if (fileExists(photoDetails.fileName)) {
                                showFileExistsDialog(this) {
                                    downloadPhoto(this, photoDetails)
                                }
                            } else {
                                downloadPhoto(this, photoDetails)
                            }
                        }
                    } else if (it.id == R.id.set_wallpaper_option) {
                        requireActivity().run {
                            if (fileExists(photoDetails.fileName)) {
                                getUriForPhoto(photoDetails.fileName)?.also { uri ->
                                    viewModel.setWallpaper(uri, requireActivity())
                                } ?: run {
                                    setWallpaper(this, photoDetails)
                                }
                            } else {
                                setWallpaper(this, photoDetails)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadPhoto(context: Context, photoDetails: PhotoDetails) {
        if (context.hasWritePermission()) {
            showDownloadQualityChoice(context, photoDetails) {
                viewModel.downloadPhoto(
                    it,
                    photoDetails.fileName,
                    photoDetails.id,
                    requireActivity(),
                    viewLifecycleOwner
                )
            }
        } else {
            permissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun setWallpaper(context: Context, photoDetails: PhotoDetails) {
        if (context.hasWritePermission()) {
            showDownloadQualityChoice(context, photoDetails) {
                viewModel.downloadAndSetWallpaper(
                    it,
                    photoDetails.fileName,
                    photoDetails.id,
                    requireActivity(),
                    viewLifecycleOwner
                )
            }
        } else {
            permissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showDownloadQualityChoice(
        context: Context,
        photoDetails: PhotoDetails,
        url: ((String) -> Unit)?
    ) {
        val items = arrayOf("Raw", "Regular", "Full", "Small", "Thumb")
        val urls = listOf(
            photoDetails.rawUrl,
            photoDetails.regularUrl,
            photoDetails.fullUrl,
            photoDetails.smallUrl,
            photoDetails.thumbUrl
        )
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.title_photo_quality)
            .setItems(items) { _, index -> url?.invoke(urls[index]) }
            .show()
    }

    private fun setupToolbar(photoDetails: PhotoDetails) {
        binding?.apply {
            val activity = requireActivity() as AppCompatActivity
            toolbar.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.toolbar_color
                )
            )
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)

            upBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            toolbarTitle.text = photoDetails.username

            Glide.with(avatar)
                .load(photoDetails.profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(avatar)
                .clearOnDetach()

            // show/hide toolbar on click anywhere on screen
            layout.setOnClickListener {
                activity.supportActionBar?.apply {
                    if (isShowing) {
                        hide()
                    } else {
                        show()
                    }
                }
                if (fab.isVisible) fab.hide() else fab.show()
            }

            info.setOnClickListener {
                activityFragmentManager.run {
                    showDialogFragment(InfoBottomSheet.newInstance(photoDetails.tags) {
                        dismissCurrentDialog()
                        findNavController().navigate(DetailsFragmentDirections.searchAction(it))
                    })
                }
            }
        }
    }

}
