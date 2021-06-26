package com.alvindizon.tampisaw.data.wallpaper

import android.content.ContentResolver
import android.content.Context
import androidx.work.*
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.data.download.ImageDownloader
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class WallpaperSettingManagerTest {

    private val contentResolver: ContentResolver = mockk()

    private val context: Context = mockk()

    private lateinit var SUT: WallpaperSettingManager

    @BeforeEach
    fun setUp() {
        SUT = WallpaperSettingManagerImpl(contentResolver)
        mockkStatic(ImageDownloader::class)
        mockkStatic(WorkManager::class)
        mockkConstructor(OneTimeWorkRequest.Builder::class)

        every { ImageDownloader.enqueueDownload(any(), any(), any(), any()) } returns mockk()
        every { ImageDownloader.cancelWorkById(any(), any()) } returns mockk()
        every { ImageDownloader.getWorkInfoByIdLiveData(any(), any()) } returns mockk()
        every {
            anyConstructed<OneTimeWorkRequest.Builder>().setInputData(any()).build()
        } returns mockk()
        every { workDataOf(any())} returns mockk()
        every { WorkManager.getInstance(any()).enqueue(any<WorkRequest>()) } returns mockk()
        every { WorkManager.getInstance(any()).cancelWorkById(any()) } returns mockk()

    }

    @Test
    fun `given download is enqueued verify ImageDownloader enqueueDownload is called and status is set to Started`() {
        SUT.enqueueDownload(QUALITY, FILENAME, ID, context, true)

        verify(exactly = 1) {
            ImageDownloader.enqueueDownload(any(), any(), any(), any())
        }
    }

    companion object {
        private const val QUALITY = "full"
        private const val FILENAME = "ASGAASGAS"
        private const val ID = "id"
    }
}