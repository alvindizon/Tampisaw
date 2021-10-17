package com.alvindizon.tampisaw.data.wallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.work.WorkInfo
import com.alvindizon.tampisaw.data.download.ImageDownloader
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class DownloadPhotoException(message: String? = null) : Exception(message)

interface WallpaperSettingManager {
    fun cancelDownload(uuid: UUID, context: Context)
    fun setWallpaper(uri: Uri, activity: Activity)
    fun setWallpaperByBitmap(uri: Uri)
    fun downloadPhoto(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ): Completable
}

@Singleton
class WallpaperSettingManagerImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val wallpaperManager: WallpaperManager
) : WallpaperSettingManager {

    override fun cancelDownload(uuid: UUID, context: Context) {
        ImageDownloader.cancelWorkById(uuid, context)
    }

    @Throws(IllegalArgumentException::class)
    override fun setWallpaper(uri: Uri, activity: Activity) {
        // try to set wallpaper using com.android.wallcropper, if it doesn't exist throw exception
        activity.startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri))
    }

    @Suppress("DEPRECATION")
    @Throws(IOException::class, FileNotFoundException::class)
    override fun setWallpaperByBitmap(uri: Uri) {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(contentResolver, uri)
            )
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
        wallpaperManager.setBitmap(bitmap)
    }

    override fun downloadPhoto(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ): Completable {
        val workData = ImageDownloader.createInputData(quality, fileName, id)
        val uuid = ImageDownloader.enqueueDownload(workData, context)
        val workInfoLiveData = ImageDownloader.getWorkInfoByIdLiveData(uuid, context)
        return Completable.create { emitter ->
            Observable.fromPublisher(
                LiveDataReactiveStreams.toPublisher(
                    lifecycleOwner,
                    workInfoLiveData
                )
            ).blockingForEach {
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> emitter.onComplete()
                    WorkInfo.State.FAILED -> emitter.onError(DownloadPhotoException("Download failed"))
                    WorkInfo.State.CANCELLED -> emitter.onError(DownloadPhotoException("Download cancelled"))
                    else -> Unit
                }
            }
        }
    }
}
