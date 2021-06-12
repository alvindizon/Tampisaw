package com.alvindizon.tampisaw

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.data.wallpaper.DownloadStatus
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

sealed class WallpaperSettingStatus(val downloadStatus: DownloadStatus? = null) {
    class DownloadStarted(downloadStatus: DownloadStatus) : WallpaperSettingStatus(downloadStatus)
    class DownloadSuccess(downloadStatus: DownloadStatus) : WallpaperSettingStatus(downloadStatus)
    class DownloadFailed(downloadStatus: DownloadStatus) : WallpaperSettingStatus(downloadStatus)
    class SetWallpaper(val uri: Uri) : WallpaperSettingStatus()
    object SettingWallpaper : WallpaperSettingStatus()
    object WallpaperSet : WallpaperSettingStatus()
    data class Error(val message: String) : WallpaperSettingStatus()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wallpaperSettingManager: WallpaperSettingManager
) :
    BaseViewModel() {

    private val _wallpaperSettingStatus: MutableLiveData<WallpaperSettingStatus> =
        MediatorLiveData<WallpaperSettingStatus>().apply {
            addSource(wallpaperSettingManager.downloadStatus) { downloadStatus ->
                when (downloadStatus) {
                    is DownloadStatus.Started -> value = WallpaperSettingStatus.DownloadStarted(
                        downloadStatus
                    )
                    is DownloadStatus.Success -> value =
                        WallpaperSettingStatus.DownloadSuccess(downloadStatus)
                    DownloadStatus.Failed -> value =
                        WallpaperSettingStatus.DownloadFailed(downloadStatus)
                    is DownloadStatus.SetWallpaper ->
                        value = WallpaperSettingStatus.SetWallpaper(downloadStatus.uri)
                    else -> Unit
                }
            }
        }
    val wallpaperSettingStatus = _wallpaperSettingStatus

    fun setBitmapAsWallpaper(uri: Uri) {
        compositeDisposable += wallpaperSettingManager.setBitmapAsWallpaper(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { wallpaperSettingStatus.value = WallpaperSettingStatus.SettingWallpaper }
            .subscribeBy(
                onComplete = {
                    wallpaperSettingStatus.value = WallpaperSettingStatus.WallpaperSet
                },
                onError = { error ->
                    error.printStackTrace()
                    wallpaperSettingStatus.value = error.message?.let {
                        WallpaperSettingStatus.Error(it)
                    }
                }
            )
    }

    fun cancelDownload(uuid: UUID, context: Context) {
        wallpaperSettingManager.cancelDownload(uuid, context)
    }

    fun onStartDownload(uuid: UUID, context: Context) {
        wallpaperSettingManager.onStartDownload(uuid, context)
    }
}
