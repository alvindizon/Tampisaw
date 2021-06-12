package com.alvindizon.tampisaw

import android.app.WallpaperManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alvindizon.tampisaw.core.ui.BaseActivity
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.core.utils.showSnackbar
import com.alvindizon.tampisaw.data.wallpaper.DownloadStatus
import com.alvindizon.tampisaw.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        observeWallpaperSettingStatus()
    }

    private fun setupNavigation() {
        // Since we're using FragmentContainerView, use findFragmentById instead of findNavController(R.id.nav_host_fragment_txn)
        // See https://stackoverflow.com/a/59275182/4612653
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_txn) as NavHostFragment? ?: return

        val navController = host.navController
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, dest, _ ->
            binding.bottomNavView.isVisible = dest.id != R.id.details_dest
        }
    }


    private fun observeWallpaperSettingStatus() {
        viewModel.wallpaperSettingStatus.observe(this) { status ->
            when (status) {
                is WallpaperSettingStatus.DownloadStarted -> {
                    val downloadStatus = status.downloadStatus as DownloadStatus.Started
                    binding.root.rootView.showSnackbar(
                        getString(R.string.wallpaper_setting_download),
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.cancel,
                        ContextCompat.getColor(this, R.color.cancel_red),
                        binding.bottomNavView
                    ) {
                        viewModel.cancelDownload(downloadStatus.uuid, this)
                    }
                    viewModel.onStartDownload(downloadStatus.uuid, this)
                }
                is WallpaperSettingStatus.DownloadSuccess -> {
                    val downloadStatus = status.downloadStatus as DownloadStatus.Success
                    if (downloadStatus.setWallpaper == true) {
                        downloadStatus.fileName?.let {
                            getUriForPhoto(it)?.let { uri ->
                                setWallpaper(uri)
                            }
                        }
                    }
                }
                WallpaperSettingStatus.SettingWallpaper -> {
                    binding.root.rootView.showSnackbar(
                        getString(R.string.wallpaper_setting_ongoing),
                        Snackbar.LENGTH_INDEFINITE,
                        null,
                        null,
                        binding.bottomNavView
                    )
                }
                WallpaperSettingStatus.WallpaperSet -> {
                    binding.root.rootView.showSnackbar(
                        getString(R.string.wallpaper_setting_success),
                        Snackbar.LENGTH_SHORT,
                        null,
                        null,
                        binding.bottomNavView
                    )
                }
                is WallpaperSettingStatus.DownloadFailed -> {
                    binding.root.rootView.showSnackbar(
                        getString(R.string.wallpaper_download_error),
                        Snackbar.LENGTH_LONG,
                        null,
                        null,
                        binding.bottomNavView
                    )
                }
                is WallpaperSettingStatus.Error ->
                    binding.root.rootView.showSnackbar(
                        status.message,
                        Snackbar.LENGTH_SHORT,
                        null,
                        null,
                        binding.bottomNavView
                    )
                is WallpaperSettingStatus.SetWallpaper -> setWallpaper(status.uri)
            }
        }
    }

    private fun setWallpaper(uri: Uri) {
        try {
            startActivity(
                WallpaperManager.getInstance(this).getCropAndSetWallpaperIntent(uri)
            )
        } catch (e: IllegalArgumentException) {
            viewModel.setBitmapAsWallpaper(uri)
        }
    }
}