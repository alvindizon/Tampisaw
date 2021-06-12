package com.alvindizon.tampisaw.data.wallpaper

import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.WorkInfo
import com.alvindizon.tampisaw.data.download.ImageDownloader
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

sealed class DownloadStatus(val fileName: String? = null, val setWallpaper: Boolean? = null) {
    class Started(val uuid: UUID, fileName: String, setWallpaper: Boolean) :
        DownloadStatus(fileName, setWallpaper)

    class Success(fileName: String?, setWallpaper: Boolean?) :
        DownloadStatus(fileName, setWallpaper)

    class SetWallpaper(val uri: Uri) : DownloadStatus()
    object Failed : DownloadStatus()
}

interface WallpaperSettingManager {
    fun enqueueDownload(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        setWallpaper: Boolean
    )

    fun onStartDownload(uuid: UUID, context: Context)
    fun cancelDownload(uuid: UUID, context: Context)
    fun setBitmapAsWallpaper(uri: Uri): Completable
    fun setWallpaperByUri(uri: Uri)
    val downloadStatus: LiveData<DownloadStatus>
}

class WallpaperSettingManagerImpl(
    private val contentResolver: ContentResolver,
    private val wallpaperManager: WallpaperManager
) :
    WallpaperSettingManager {
    private val _downloadStatus = MediatorLiveData<DownloadStatus>()
    override val downloadStatus = _downloadStatus

    override fun enqueueDownload(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        setWallpaper: Boolean
    ) {
        val requestId = ImageDownloader.enqueueDownload(quality, fileName, id, context)
        _downloadStatus.value = DownloadStatus.Started(requestId, fileName, setWallpaper)
    }

    override fun onStartDownload(uuid: UUID, context: Context) {
        with(_downloadStatus) {
            addSource(ImageDownloader.getWorkInfoByIdLiveData(uuid, context)) { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val prev = value
                    value = DownloadStatus.Success(prev?.fileName, prev?.setWallpaper)
                } else if (workInfo.state == WorkInfo.State.FAILED) {
                    value = DownloadStatus.Failed
                }
            }
        }
    }

    override fun cancelDownload(uuid: UUID, context: Context) {
        ImageDownloader.cancelWorkById(uuid, context)
    }

    @Suppress("DEPRECATION")
    override fun setBitmapAsWallpaper(uri: Uri): Completable {
        return Completable.create { emitter ->
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(contentResolver, uri)
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                wallpaperManager.setBitmap(bitmap)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(Exception("Error decoding bitmap.", e))
            }
        }
    }

    override fun setWallpaperByUri(uri: Uri) {
        _downloadStatus.value = DownloadStatus.SetWallpaper(uri)
    }
}