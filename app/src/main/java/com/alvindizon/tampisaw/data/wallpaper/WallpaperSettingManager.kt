package com.alvindizon.tampisaw.data.wallpaper

import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.alvindizon.tampisaw.data.download.ImageDownloader
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

interface WallpaperSettingManager {
    fun enqueueDownload(
        quality: String,
        fileName: String,
        id: String,
        context: Context
    ): UUID
    fun cancelDownload(uuid: UUID, context: Context)
    fun setBitmapAsWallpaper(uri: Uri)
}

class WallpaperSettingManagerImpl(
    private val contentResolver: ContentResolver,
    private val wallpaperManager: WallpaperManager
) : WallpaperSettingManager {

    override fun enqueueDownload(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
    ): UUID = ImageDownloader.enqueueDownload(quality, fileName, id, context)

    override fun cancelDownload(uuid: UUID, context: Context) {
        ImageDownloader.cancelWorkById(uuid, context)
    }

    @Suppress("DEPRECATION")
    @Throws(IOException::class, FileNotFoundException::class)
    override fun setBitmapAsWallpaper(uri: Uri) {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(contentResolver, uri)
            )
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
        wallpaperManager.setBitmap(bitmap)
    }
}
