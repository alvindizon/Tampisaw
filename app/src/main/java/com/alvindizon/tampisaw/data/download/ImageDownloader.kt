package com.alvindizon.tampisaw.data.download

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LiveData
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.impl.utils.futures.SettableFuture
import androidx.work.rxjava3.RxWorker
import androidx.work.workDataOf
import com.alvindizon.tampisaw.core.ui.NotifsHelper
import com.alvindizon.tampisaw.core.utils.FILE_PROVIDER_AUTHORITY
import com.alvindizon.tampisaw.core.utils.TAMPISAW_LEGACY_PATH
import com.alvindizon.tampisaw.core.utils.TAMPISAW_RELATIVE_PATH
import com.alvindizon.tampisaw.data.networking.api.UnsplashApi
import com.google.common.util.concurrent.ListenableFuture
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.util.*

class DownloadCancelledException(message: String) : IOException(message)

@HiltWorker
class ImageDownloader @AssistedInject constructor(
    private val unsplashApi: UnsplashApi,
    private val notifsHelper: NotifsHelper,
    private val contentResolver: ContentResolver,
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : RxWorker(context, params) {

    @SuppressLint("RestrictedApi")
    override fun getForegroundInfoAsync(): ListenableFuture<ForegroundInfo> {
        val future = SettableFuture.create<ForegroundInfo>()

        val notificationId = id.hashCode()
        val fileName = inputData.getString(KEY_OUTPUT_FILE_NAME)

        if (fileName == null) {
            future.setException(IllegalStateException("Filename is null"))
            return future
        }
        // note: at this point, WorkManager has been manually initialized in WorkerModule,
        // thus it is safe to call WorkManager.getInstance here
        val notificationBuilder = getNotificationBuilder(fileName)

        future.set(ForegroundInfo(notificationId, notificationBuilder.build()))
        return future
    }

    override fun createWork(): Single<Result> {
        val url = inputData.getString(KEY_INPUT_URL)
        val fileName = inputData.getString(KEY_OUTPUT_FILE_NAME)
        val photoId = inputData.getString(KEY_PHOTO_ID)

        if (url == null || fileName == null || photoId == null) return Single.just(Result.failure())

        val notificationId = id.hashCode()
        // note: at this point, WorkManager has been manually initialized in WorkerModule,
        // thus it is safe to call WorkManager.getInstance here
        val notificationBuilder = getNotificationBuilder(fileName)

        return unsplashApi.downloadFile(url)
            .flatMap {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImage(it, fileName, notificationId, notificationBuilder)
                } else {
                    saveImageLegacy(it, fileName, notificationId, notificationBuilder)
                }
            }
            .flatMapCompletable {
                notifsHelper.showDownloadCompleteNotification(fileName, it)
                unsplashApi.trackDownload(photoId)
            }
            .toSingle { Result.success() }
            .onErrorReturn {
                it.printStackTrace()
                notifsHelper.showDownloadErrorNotification(fileName)
                Result.failure()
            }
    }

    private fun getNotificationBuilder(fileName: String): NotificationCompat.Builder {
        val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)
        return notifsHelper.getDownloadProgressNotifBuilder(fileName, cancelIntent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImage(
        responseBody: ResponseBody,
        fileName: String,
        notificationId: Int,
        builder: NotificationCompat.Builder
    ): Single<Uri> {
        return Single.create { emitter ->

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.TITLE, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / MILLIS)
                put(MediaStore.Images.Media.SIZE, responseBody.contentLength())
                put(MediaStore.Images.Media.RELATIVE_PATH, TAMPISAW_RELATIVE_PATH)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                val complete = contentResolver.openOutputStream(uri)?.use { outputStream ->
                    responseBody.writeToSink(outputStream.sink().buffer()) {
                        setForegroundAsync(
                            ForegroundInfo(
                                notificationId,
                                notifsHelper.updateDownloadNotification(builder, it).build()
                            )
                        )
                    }
                } ?: false

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, values, null, null)

                if (!complete) {
                    contentResolver.delete(uri, null, null)
                    emitter.tryOnError(DownloadCancelledException("Cancelled by user"))
                    return@create
                }

                emitter.onSuccess(it)
                return@create
            }

            emitter.tryOnError(RuntimeException("null uri"))
        }

    }

    private fun saveImageLegacy(
        responseBody: ResponseBody,
        fileName: String,
        notificationId: Int,
        builder: NotificationCompat.Builder
    ): Single<Uri> {
        return Single.create { emitter ->
            val path = File(TAMPISAW_LEGACY_PATH)

            if (!path.exists()) {
                if (!path.mkdirs()) {
                    emitter.tryOnError(RuntimeException("Error creating directory"))
                    return@create
                }
            }

            val file = File(path, fileName)

            val complete = responseBody.writeToSink(file.sink().buffer()) {
                setForegroundAsync(
                    ForegroundInfo(
                        notificationId,
                        notifsHelper.updateDownloadNotification(builder, it).build()
                    )
                )
            }

            if (!complete && file.exists()) {
                file.delete()
                emitter.tryOnError(DownloadCancelledException("Cancelled by user"))
                return@create
            }

            MediaScannerConnection.scanFile(
                context, arrayOf(file.absolutePath),
                arrayOf("image/jpeg"), null
            )

            // we need to use FileProvider since we're targeting >= API 24, if not then using
            // Uri.fromFile would cause FileUriExposedException
            val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
            emitter.onSuccess(uri)
        }

    }

    private fun ResponseBody.writeToSink(
        sink: BufferedSink,
        onProgress: ((Int) -> Unit)?
    ): Boolean {
        val fileSize = contentLength()

        var totalBytesRead = 0L
        var progressToReport = 0

        while (true) {
            if (isStopped) return false
            val readCount = source().read(sink.buffer, MAX_BYTE)
            if (readCount == END) break
            sink.emit()
            totalBytesRead += readCount
            val progress = (MAX_PROGRESS * totalBytesRead / fileSize)
            if (progress - progressToReport >= MIN_PROGRESS_TO_REPORT) {
                progressToReport = progress.toInt()
                onProgress?.invoke(progressToReport)
            }
        }

        sink.close()
        return true
    }

    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
        const val KEY_PHOTO_ID = "KEY_PHOTO_ID"
        private const val END = -1L
        private const val MAX_BYTE = 8192L
        private const val MAX_PROGRESS = 100.0
        private const val MIN_PROGRESS_TO_REPORT = 10
        private const val MILLIS = 1000

        fun createInputData(
            url: String,
            fileName: String,
            photoId: String?,
        ): Data = workDataOf(
            KEY_INPUT_URL to url,
            KEY_OUTPUT_FILE_NAME to fileName,
            KEY_PHOTO_ID to photoId
        )

        fun enqueueDownload(
            workData: Data,
            context: Context
        ): UUID {
            val request =
                OneTimeWorkRequestBuilder<ImageDownloader>().apply {
                    setInputData(workData)
                    setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                }.build()
            WorkManager.getInstance(context).enqueue(request)
            return request.id
        }

        fun cancelWorkById(id: UUID, context: Context) {
            WorkManager.getInstance(context).cancelWorkById(id)
        }

        fun getWorkInfoByIdLiveData(id: UUID, context: Context): LiveData<WorkInfo> =
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(id)
    }
}
