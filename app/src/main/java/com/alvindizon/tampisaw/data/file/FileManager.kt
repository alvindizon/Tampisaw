package com.alvindizon.tampisaw.data.file

import android.content.Context
import android.net.Uri
import com.alvindizon.tampisaw.core.utils.getUriForPhoto
import com.alvindizon.tampisaw.data.wallpaper.DownloadPhotoException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface FileManager {
    fun getUriForPhoto(context: Context, fileName: String): Single<Uri>
}

// TODO change the way we get URIs to a more unit testable way
class FileManagerImpl @Inject constructor() : FileManager {

    override fun getUriForPhoto(context: Context, fileName: String): Single<Uri> {
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
