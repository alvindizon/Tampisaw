package com.alvindizon.tampisaw.domain

import android.net.Uri
import com.alvindizon.tampisaw.data.wallpaper.WallpaperSettingManager
import io.reactivex.Completable
import javax.inject.Inject

class SetWallpaperByBitmapUseCase @Inject constructor(
    private val wallpaperSettingManager: WallpaperSettingManager
) {

    fun setWallpaperByBitmap(uri: Uri): Completable {
        return Completable.create { emitter ->
            try {
                wallpaperSettingManager.setWallpaperByBitmap(uri)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }
}
