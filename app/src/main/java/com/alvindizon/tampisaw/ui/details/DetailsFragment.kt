package com.alvindizon.tampisaw.ui.details

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.hasWritePermission
import com.alvindizon.tampisaw.core.ui.BaseFragment
import com.alvindizon.tampisaw.core.utils.*
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import com.alvindizon.tampisaw.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null

    private val viewModel: DetailsViewModel by activityViewModels()

    private val args: DetailsFragmentArgs by navArgs()

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // do nothing
        }

    @Inject
    lateinit var activityFragmentManager: FragmentManager

    @Inject
    lateinit var wallpaperSettingManager: WallpaperSettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState.observe(this) { uiState ->
            binding?.progressBar?.isVisible = uiState is Loading
            binding?.image?.isVisible = uiState !is Loading
            when (uiState) {
                is GetDetailSuccess -> {
                    binding?.image?.let { imgView ->
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
                    binding?.root?.rootView?.showSnackbar(uiState.message, Snackbar.LENGTH_SHORT)
                }
                else -> Unit
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

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
                                    downloadPhoto(this, photoDetails, false)
                                }
                            } else {
                                downloadPhoto(this, photoDetails, false)
                            }
                        }
                    } else if(it.id == R.id.set_wallpaper_option) {
                        requireActivity().run {
                            if (fileExists(photoDetails.fileName)) {
                                getUriForPhoto(photoDetails.fileName)?.also { uri ->
                                    wallpaperSettingManager.setWallpaperByUri(uri)
                                } ?: run {
                                    downloadPhoto(this, photoDetails, true)
                                }
                            } else {
                                downloadPhoto(this, photoDetails, true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadPhoto(context: Context, photoDetails: PhotoDetails, setWallpaper: Boolean) {
        if (context.hasWritePermission()) {
            showDownloadQualityChoice(context, photoDetails) {
                wallpaperSettingManager.enqueueDownload(
                    it,
                    photoDetails.fileName,
                    photoDetails.id,
                    context,
                    setWallpaper
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
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.title_photo_quality)
            .setItems(items) { _, which ->
                url?.invoke(
                    when (which) {
                        0 -> photoDetails.rawUrl
                        1 -> photoDetails.regularUrl
                        2 -> photoDetails.fullUrl
                        3 -> photoDetails.smallUrl
                        4 -> photoDetails.thumbUrl
                        else -> photoDetails.regularUrl
                    }
                )
            }
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

            Glide.with(avatar)
                .load(photoDetails.profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(avatar)
                .clearOnDetach()

            toolbarTitle.text = photoDetails.username

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
                activityFragmentManager.showDialogFragment(InfoBottomSheet.newInstance(photoDetails.tags) {
                })
            }
        }
    }

}
