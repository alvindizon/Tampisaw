package com.alvindizon.tampisaw.data.wallpaper

import android.app.WallpaperManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.data.download.ImageDownloader
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(InstantExecutorExtension::class)
class WallpaperSettingManagerTest {

    private val contentResolver: ContentResolver = mockk()

    private val wallpaperManager: WallpaperManager = mockk()

    private val context: Context = mockk()

    private val uuid: UUID = mockk()

    private val uri: Uri = mockk()

    private lateinit var SUT: WallpaperSettingManager

    @BeforeEach
    fun setUp() {
        SUT = WallpaperSettingManagerImpl(contentResolver, wallpaperManager)
        mockkStatic(ImageDownloader::class)
        mockkStatic(WorkManager::class)
        mockkConstructor(OneTimeWorkRequest.Builder::class)

        // mocking workdata results in error(SignedCall not matching), need to create input data to match call
        val inputData = ImageDownloader.createInputData(QUALITY, FILENAME, ID)

        every { ImageDownloader.enqueueDownload(inputData, any()) } returns mockk()
        every { ImageDownloader.cancelWorkById(any(), any()) } returns mockk()
        every { ImageDownloader.getWorkInfoByIdLiveData(any(), any()) } returns mockk()
        every {
            anyConstructed<OneTimeWorkRequest.Builder>().setInputData(inputData).build()
        } returns mockk()
        every { WorkManager.getInstance(any()).enqueue(any<WorkRequest>()) } returns mockk()
        every { WorkManager.getInstance(any()).cancelWorkById(any()) } returns mockk()

        mockkStatic(MediaStore.Images.Media::class)
        @Suppress("DEPRECATION")
        every { MediaStore.Images.Media.getBitmap(any(), any()) } returns mockk()

        every { wallpaperManager.setBitmap(any())} just Runs
    }

    @Test
    fun `given download is enqueued verify ImageDownloader enqueueDownload is called`() {
        SUT.enqueueDownload(QUALITY, FILENAME, ID, context)

        verify(exactly = 1) {
            WorkManager.getInstance(any()).enqueue(any<WorkRequest>())
        }
    }

    @Test
    fun `given download is cancelled verify ImageDownloader cancelWorkById is called`() {
        SUT.cancelDownload(uuid, context)

        verify(exactly = 1) {
            WorkManager.getInstance(any()).cancelWorkById(any())
        }
    }

    @Test
    fun `given bitmap is going to be set as wallpaper verify wallpaperManager setBitmap is called`() {
        SUT.setBitmapAsWallpaper(uri)

        verify(exactly = 1) {
            wallpaperManager.setBitmap(any())
        }
    }

    companion object {
        private const val QUALITY = "full"
        private const val FILENAME = "ASGAASGAS"
        private const val ID = "id"
    }
}
