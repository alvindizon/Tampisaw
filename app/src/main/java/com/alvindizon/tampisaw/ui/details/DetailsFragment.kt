package com.alvindizon.tampisaw.ui.details

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.hasWritePermission
import com.alvindizon.tampisaw.core.requestPermission
import com.alvindizon.tampisaw.core.ui.DialogManager
import com.alvindizon.tampisaw.core.utils.fileExists
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.core.utils.showFileExistsDialog
import com.alvindizon.tampisaw.data.download.ImageDownloader
import com.alvindizon.tampisaw.databinding.FragmentDetailsBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DetailsFragment: Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null

    private var snackbar: Snackbar? = null

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
                    setupToolbar(it.photoDetails)
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
            fabLayout.forEach { item ->
                item.setOnClickListener {
                    when(it.id) {
                        R.id.faboption_1 -> {
                            if(requireContext().fileExists(photoDetails.fileName)) {
                                showFileExistsDialog(requireContext()) {
                                    downloadPhoto(photoDetails)
                                }
                            } else {
                                downloadPhoto(photoDetails)
                            }
                        }
                        R.id.faboption_2 -> {
                            if(requireContext().fileExists(photoDetails.fileName)) {
                                requireContext().getUriForPhoto(photoDetails.fileName)?.let { uri ->
                                    setWallpaper(uri)
                                }?:run {
                                    downloadWallpaper(photoDetails)
                                }
                            } else {
                                downloadWallpaper(photoDetails)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadPhoto(photoDetails: PhotoDetails) {
        if(requireContext().hasWritePermission()) {
            showDownloadQualityChoice(requireContext(), photoDetails) {
                val request = ImageDownloader.enqueueDownload(
                    it, photoDetails.fileName, photoDetails.id
                )
                workManager.enqueue(request)
            }

        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 0)
        }
    }

    private fun showDownloadQualityChoice(context: Context, photoDetails: PhotoDetails, url: ((String) -> Unit)?) {
        val items = arrayOf("Raw", "Regular", "Full", "Small", "Thumb")
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.title_photo_quality)
            .setItems(items) { _, which ->
                url?.invoke( when(which) {
                    0 -> photoDetails.rawUrl
                    1 -> photoDetails.regularUrl
                    2 -> photoDetails.fullUrl
                    3 -> photoDetails.smallUrl
                    4 -> photoDetails.thumbUrl
                    else ->photoDetails.regularUrl
                })
            }
            .show()
    }

    private fun setWallpaper(uri: Uri) {
        try {
            startActivity(WallpaperManager.getInstance(requireContext()).getCropAndSetWallpaperIntent(uri))
        } catch (e: IllegalArgumentException) {
            var bitmap: Bitmap? = null
            try {
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, uri))
                } else {
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                }
                WallpaperManager.getInstance(requireActivity().applicationContext).setBitmap(bitmap)
                binding?.root?.rootView?.let {
                    Snackbar.make(it,"Wallpaper set successfully", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding?.root?.rootView?.let {
                    Snackbar.make(it,"Error setting wallpaper: ${e.message}", Snackbar.LENGTH_LONG).show()
                }
            } finally {
                bitmap?.recycle()
            }
        }
    }

    private fun downloadWallpaper(photoDetails: PhotoDetails) {
        if(requireContext().hasWritePermission()) {
            showDownloadQualityChoice(requireContext(), photoDetails) {
                val request = ImageDownloader.enqueueDownload(
                    it, photoDetails.fileName, photoDetails.id
                )
                workManager.enqueue(request)

                binding?.root?.rootView?.let {view ->
                    snackbar = Snackbar.make(view,"Setting wallpaper...", Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.cancel) {
                            workManager.cancelWorkById(request.id)
                        }
                        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.cancel_red))

                    snackbar?.show()
                }

                workManager.getWorkInfoByIdLiveData(request.id).observe(viewLifecycleOwner,
                    { workInfo ->
                        if(workInfo.state == WorkInfo.State.SUCCEEDED) {
                            snackbar?.dismiss()
                            requireContext().getUriForPhoto(photoDetails.fileName)?.let { uri ->
                                setWallpaper(uri)
                            }
                        } else if(workInfo.state == WorkInfo.State.FAILED) {
                            binding?.root?.rootView?.let { view ->
                                Snackbar.make(view ,"Error downloading wallpaper.", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    })
            }
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 0)
        }
    }

    private fun setupToolbar(photoDetails: PhotoDetails) {
        binding?.apply{
            val activity = requireActivity() as AppCompatActivity
            toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolbar_color))
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

            // show/hide toolbar and FAB on click anywhere on screen
            layout.setOnClickListener {
                activity.supportActionBar?.apply {
                    if(isShowing) {
                        hide()
                    } else {
                        show()
                    }
                }
                if(fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }

            info.setOnClickListener {
                dialogManager.showDialog( InfoBottomSheet.newInstance(photoDetails.tags){ tag ->
                    Log.d("DetailsFragment", "tag: $tag")
                })
            }
        }
    }

}
