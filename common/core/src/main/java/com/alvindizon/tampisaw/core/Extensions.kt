package com.alvindizon.tampisaw.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow


fun Int.toCompactFormat(): String {
    if (this < 1000) return "$this"
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        Locale.ENGLISH,
        "%.1f%c",
        this / 1000.0.pow(exp.toDouble()),
        "KMGTPE"[exp - 1]
    )
}

fun Context.hasWritePermission(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
            hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Context.hasPermission(vararg permissions: String): Boolean {
    return permissions.all { singlePermission ->
        ContextCompat.checkSelfPermission(
            this,
            singlePermission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
