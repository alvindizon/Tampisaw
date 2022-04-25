package com.alvindizon.tampisaw.domain

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.setwallpaper.DownloadPhotoException
import com.alvindizon.tampisaw.setwallpaper.WallpaperSettingManager
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(private val wallpaperSettingManager: WallpaperSettingManager) {
    fun downloadPhoto(
        quality: String,
        fileName: String,
        id: String,
        activity: Activity,
        lifecycleOwner: LifecycleOwner
    ): Single<Uri> {
        return wallpaperSettingManager.downloadPhoto(
            quality,
            fileName,
            id,
            activity,
            lifecycleOwner
        ).andThen(getUriForPhoto(activity, fileName))
    }

    private fun getUriForPhoto(context: Context, fileName: String): Single<Uri> {
        return Single.defer {
            val uri = context.getUriForPhoto(fileName)
            if (uri != null) {
                Single.just(uri)
            } else {
                Single.error(DownloadPhotoException())
            }
        }
    }
}
