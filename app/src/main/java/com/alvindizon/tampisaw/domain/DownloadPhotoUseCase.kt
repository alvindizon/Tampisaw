package com.alvindizon.tampisaw.domain

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.work.WorkInfo
import com.alvindizon.tampisaw.data.download.ImageDownloader
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(private val wallpaperSettingManager: WallpaperSettingManager) {

    fun downloadPhoto(
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
                    emitter.onComplete()
                }
            }
        }
    }
}
