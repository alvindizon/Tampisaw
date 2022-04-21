package com.alvindizon.tampisaw.core.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alvindizon.tampisaw.core.R
import javax.inject.Inject

class NotifsHelper @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat
) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannels = listOf(
                NotificationChannel(
                    DOWNLOADS_CHANNEL_ID,
                    "Downloads",
                    NotificationManager.IMPORTANCE_LOW
                ),
            )
            notificationManager.createNotificationChannels(notificationChannels)
        }
    }

    fun getDownloadProgressNotifBuilder(fileName: String, cancelIntent: PendingIntent) =
        NotificationCompat.Builder(context, DOWNLOADS_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_LOW
            setSmallIcon(android.R.drawable.stat_sys_download)
            setTicker("")
            setOngoing(true)
            setContentTitle(fileName)
            setProgress(0, 0, true)
            addAction(0, context.getString(R.string.cancel), cancelIntent)
        }

    fun updateDownloadNotification(
        builder: NotificationCompat.Builder,
        progress: Int
    ) = builder.apply {
        setProgress(MAX_PROGRESS, progress, false)
        if (progress == MAX_PROGRESS) setSmallIcon(android.R.drawable.stat_sys_download_done)
    }

    fun showDownloadCompleteNotification(fileName: String, uri: Uri) {
        val builder = NotificationCompat.Builder(context, DOWNLOADS_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_LOW
            setSmallIcon(android.R.drawable.stat_sys_download_done)
            setContentTitle(fileName)
            setContentText(context.getString(R.string.download_complete))
            setContentIntent(getViewWithIntent(uri))
            setProgress(0, 0, false)
            setAutoCancel(true)
        }
        notificationManager.notify(fileName.hashCode(), builder.build())
    }

    fun showDownloadErrorNotification(fileName: String) {
        val builder = NotificationCompat.Builder(context, DOWNLOADS_CHANNEL_ID).apply {
            priority = NotificationCompat.PRIORITY_LOW
            setSmallIcon(android.R.drawable.stat_sys_download_done)
            setContentTitle(fileName)
            setContentText(context.getString(R.string.err_download))
            setProgress(0, 0, false)
        }
        notificationManager.notify(fileName.hashCode(), builder.build())
    }

    // On notif click, show various options for displaying downloaded image
    private fun getViewWithIntent(uri: Uri): PendingIntent {
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            setDataAndType(uri, "image/*")
        }

        val chooser = Intent.createChooser(viewIntent, "Open with")
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, 0, chooser, flag)
    }

    companion object {
        private const val DOWNLOADS_CHANNEL_ID = "downloads_channel_id"
        private const val MAX_PROGRESS = 100
    }

}
