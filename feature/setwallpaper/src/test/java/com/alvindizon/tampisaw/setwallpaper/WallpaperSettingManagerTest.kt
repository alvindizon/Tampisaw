package com.alvindizon.tampisaw.setwallpaper

import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.alvindizon.tampisaw.downloadwallpaper.ImageDownloader
import com.alvindizon.tampisaw.testbase.InstantExecutorExtension
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(InstantExecutorExtension::class)
class WallpaperSettingManagerTest {

    private val contentResolver: ContentResolver = mockk()

    private val wallpaperManager: WallpaperManager = mockk()

    private val context: Context = mockk()

    private val activity: FragmentActivity = mockk()

    private val uuid: UUID = mockk()

    private val uri: Uri = mockk()

    private val lifecycleOwner: LifecycleOwner = mockk()

    private lateinit var wallpaperSettingManager: WallpaperSettingManager

    @BeforeEach
    fun setUp() {
        wallpaperSettingManager = WallpaperSettingManagerImpl(contentResolver, wallpaperManager)
        mockkStatic(ImageDownloader::class)
        mockkStatic(WorkManager::class)
        mockkConstructor(OneTimeWorkRequest.Builder::class)

        every { ImageDownloader.enqueueDownload(inputData, any()) } returns mockk()
        every { ImageDownloader.cancelWorkById(any(), any()) } returns mockk()
        every { ImageDownloader.getWorkInfoByIdLiveData(any(), any()) } returns mockk()
        every {
            anyConstructed<OneTimeWorkRequest.Builder>().setInputData(inputData)
                .build()
        } returns mockk()
        every { WorkManager.getInstance(any()).enqueue(any<WorkRequest>()) } returns mockk()
        every { WorkManager.getInstance(any()).cancelWorkById(any()) } returns mockk()

        mockkStatic(MediaStore.Images.Media::class)
        @Suppress("DEPRECATION")
        every { MediaStore.Images.Media.getBitmap(any(), any()) } returns mockk()

        every { wallpaperManager.setBitmap(any()) } just Runs
        every { activity.startActivity(any()) } just Runs
        every { wallpaperManager.getCropAndSetWallpaperIntent(any()) } returns mockk()
    }

    @Test
    fun `given download is enqueued verify ImageDownloader enqueueDownload is called`() {

        wallpaperSettingManager.downloadPhoto(QUALITY, FILENAME, ID, context, lifecycleOwner)
        // todo fix matching calls when verify set exactly to 1
        verify {
            WorkManager.getInstance(any()).enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `given download is cancelled verify ImageDownloader cancelWorkById is called`() {
        wallpaperSettingManager.cancelDownload(uuid, context)

        verify(exactly = 1) {
            WorkManager.getInstance(any()).cancelWorkById(any())
        }
    }

    @Test
    fun `given setWallpaper is called, verify getCropAndSetWallpaperIntent is called`() {
        wallpaperSettingManager.setWallpaper(uri, activity)

        verify(exactly = 1) {
            wallpaperManager.getCropAndSetWallpaperIntent(any())
        }
    }

    @Test
    fun `given setWallpaperByBitmap is called, verify setBitmap is called`() {
        wallpaperSettingManager.setWallpaperByBitmap(uri)

        verify(exactly = 1) {
            wallpaperManager.setBitmap(any())
        }
    }

    companion object {
        private const val QUALITY = "full"
        private const val FILENAME = "ASGAASGAS"
        private const val ID = "id"
        // mocking workdata results in error(SignedCall not matching), need to create input data to match call
        private val inputData = ImageDownloader.createInputData(QUALITY, FILENAME, ID)
    }
}
