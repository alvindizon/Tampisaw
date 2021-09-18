package com.alvindizon.tampisaw.core.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.alvindizon.tampisaw.BuildConfig
import com.alvindizon.tampisaw.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

const val TAMPISAW_DIRECTORY = "Tampisaw"

const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

val TAMPISAW_RELATIVE_PATH = "${Environment.DIRECTORY_PICTURES}${File.separator}$TAMPISAW_DIRECTORY"

@Suppress("DEPRECATION")
val TAMPISAW_LEGACY_PATH = "${
    Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
}${File.separator}$TAMPISAW_DIRECTORY"


fun Context.fileExists(fileName: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} like ? and " +
                "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"

        val relativePath = TAMPISAW_RELATIVE_PATH
        val selectionArgs = arrayOf("%$relativePath%", fileName)
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
            it.count > 0
        } ?: false
    } else {
        File(TAMPISAW_LEGACY_PATH, fileName).exists()
    }

}

fun Context.getUriForPhoto(fileName: String): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} like ? and " +
                "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"

        val relativePath = TAMPISAW_RELATIVE_PATH
        val selectionArgs = arrayOf("%$relativePath%", fileName)
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
            if (it.moveToFirst()) {
                ContentUris.withAppendedId(
                    uri, it.getLong(
                        it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                    )
                )
            } else {
                null
            }
        }
    } else {
        val file = File(TAMPISAW_LEGACY_PATH, fileName)
        FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, file)
    }
}

fun showFileExistsDialog(context: Context, action: () -> Unit) {
    MaterialAlertDialogBuilder(context)
        .setTitle(R.string.title_download_again)
        .setMessage(R.string.download_again)
        .setPositiveButton(R.string.yes) { _, _ -> action.invoke() }
        .setNegativeButton(R.string.no, null)
        .show()
}
