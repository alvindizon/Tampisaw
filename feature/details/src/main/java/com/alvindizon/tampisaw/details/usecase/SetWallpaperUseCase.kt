package com.alvindizon.tampisaw.details.usecase

import android.app.Activity
import android.net.Uri
import com.alvindizon.tampisaw.setwallpaper.WallpaperSettingManager
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(
    private val wallpaperSettingManager: WallpaperSettingManager
) {

    fun setWallpaper(uri: Uri, activity: Activity): Completable {
        return Completable.create { emitter ->
            try {
                wallpaperSettingManager.setWallpaper(uri, activity)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }
}
