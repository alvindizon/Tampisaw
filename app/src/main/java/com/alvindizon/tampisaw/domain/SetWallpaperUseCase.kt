package com.alvindizon.tampisaw.domain

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.work.WorkInfo
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.data.download.ImageDownloader
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(private val wallpaperSettingManager: WallpaperSettingManager) {

    fun downloadAndSetWallpaper(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ): Completable {
        return Completable.create { emitter ->
            val uuid =
                wallpaperSettingManager.enqueueDownload(quality, fileName, id, context)
            val workInfoLiveData = ImageDownloader.getWorkInfoByIdLiveData(uuid, context)

            Flowable.fromPublisher(
                LiveDataReactiveStreams.toPublisher(
                    lifecycleOwner,
                    workInfoLiveData
                )
            ).blockingForEach { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val uri = context.getUriForPhoto(fileName)
                    if (uri != null) {
                        try {
                            wallpaperSettingManager.setBitmapAsWallpaper(uri)
                            emitter.onComplete()
                        } catch (e: Exception) {
                            emitter.onError(e)
                        }
                    } else {
                        emitter.onError(SetWallpaperError())
                    }
                }
            }
        }
    }

    fun setWallpaperByUri(uri: Uri): Completable {
        return Completable.create { emitter ->
            try {
                wallpaperSettingManager.setBitmapAsWallpaper(uri)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}

class SetWallpaperError: Exception()
