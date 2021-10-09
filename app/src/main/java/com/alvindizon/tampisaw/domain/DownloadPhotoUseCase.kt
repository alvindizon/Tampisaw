package com.alvindizon.tampisaw.domain

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.alvindizon.tampisaw.data.file.FileManager
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.reactivex.Single
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(
    private val wallpaperSettingManager: WallpaperSettingManager,
    private val fileManager: FileManager
) {
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
        ).andThen(fileManager.getUriForPhoto(activity, fileName))
    }
}
