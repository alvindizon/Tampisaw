package com.alvindizon.tampisaw.features.details

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.alvindizon.tampisaw.api.model.getphoto.Exif
import com.alvindizon.tampisaw.api.model.getphoto.GetPhotoResponse
import com.alvindizon.tampisaw.api.model.getphoto.Links
import com.alvindizon.tampisaw.api.model.getphoto.Location
import com.alvindizon.tampisaw.api.model.getphoto.Tag
import com.alvindizon.tampisaw.api.model.getphoto.Urls
import com.alvindizon.tampisaw.api.model.getphoto.User
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.domain.DownloadPhotoUseCase
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.domain.SetWallpaperByBitmapUseCase
import com.alvindizon.tampisaw.domain.SetWallpaperUseCase
import com.alvindizon.tampisaw.testbase.CoroutineExtension
import com.alvindizon.tampisaw.testbase.InstantExecutorExtension
import com.alvindizon.tampisaw.testbase.RxSchedulerExtension
import com.alvindizon.tampisaw.testbase.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException


@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class DetailsViewModelTest {

    @MockK
    lateinit var getPhotoUseCase: GetPhotoUseCase

    @MockK
    lateinit var downloadPhotoUseCase: DownloadPhotoUseCase

    @MockK
    lateinit var setWallpaperUseCase: SetWallpaperUseCase

    @MockK
    lateinit var setWallpaperByBitmapUseCase: SetWallpaperByBitmapUseCase

    private lateinit var viewModel: DetailsViewModel

    @MockK
    lateinit var lifecycleOwner: LifecycleOwner

    @MockK
    lateinit var uri: Uri

    @MockK
    lateinit var activity: Activity

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = DetailsViewModel(
            getPhotoUseCase,
            downloadPhotoUseCase,
            setWallpaperUseCase,
            setWallpaperByBitmapUseCase
        )
    }

    @Test
    fun `observe correct states when getPhotos is successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        every { getPhotoUseCase.getPhoto(any()) } returns Single.just(getPhotoResponse)

        viewModel.getPhoto("ID")
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], GetDetailSuccess(getPhotoResponse.toPhotoDetails()))
    }

    @Test
    fun `observe correct states when getPhotos is not successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every { getPhotoUseCase.getPhoto(any()) } returns Single.error(IOException(errorMsg))

        viewModel.getPhoto("ID")
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when downloadPhotos is successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.just(uri)

        viewModel.downloadPhoto(quality, fileName, id, activity, lifecycleOwner)
        assertEquals(observedValues[0], Downloading)
        assertEquals(observedValues[1], DownloadSuccess)
    }

    @Test
    fun `observe correct states when downloadPhotos errors`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.error(Throwable(errorMsg))

        viewModel.downloadPhoto(quality, fileName, id, activity, lifecycleOwner)
        assertEquals(observedValues[0], Downloading)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when downloadAndSetWallpaper is successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.just(uri)

        every { setWallpaperUseCase.setWallpaper(any(), any()) } returns Completable.complete()

        viewModel.downloadAndSetWallpaper(quality, fileName, id, activity, lifecycleOwner)
        verify(exactly = 1) {
            setWallpaperUseCase.setWallpaper(any(), any())
        }
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `observe correct states when setWallpaper returns IllegalArgumentException and setWallpaperByBitmap is successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.just(uri)

        every { setWallpaperUseCase.setWallpaper(any(), any()) } returns Completable.error(
            IllegalArgumentException()
        )

        every { setWallpaperByBitmapUseCase.setWallpaperByBitmap(any()) } returns Completable.complete()

        viewModel.downloadAndSetWallpaper(quality, fileName, id, activity, lifecycleOwner)
        verify(exactly = 1) {
            setWallpaperByBitmapUseCase.setWallpaperByBitmap(any())
        }
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `observe correct states when downloadAndSetWallpaper errors`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.error(Throwable(errorMsg))

        viewModel.downloadAndSetWallpaper(quality, fileName, id, activity, lifecycleOwner)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when setWallpaper returns Exception`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Single.just(uri)

        every {
            setWallpaperUseCase.setWallpaper(
                any(),
                any()
            )
        } returns Completable.error(Exception(errorMsg))

        viewModel.downloadAndSetWallpaper(quality, fileName, id, activity, lifecycleOwner)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when setWallpaper is successful`() {
        val observedValues = viewModel.uiState.testObserver().observedValues

        every {
            setWallpaperUseCase.setWallpaper(any(), any())
        } returns Completable.complete()

        viewModel.setWallpaper(uri, activity)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `observe correct states when setWallpaper errors`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            setWallpaperUseCase.setWallpaper(any(), any())
        } returns Completable.error(Throwable(errorMsg))

        viewModel.setWallpaper(uri, activity)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `given setWallpaper is called, verify success when setWallpaper returns IllegalArgumentException and setWallpaperByBitmap succeeds`() {
        val observedValues = viewModel.uiState.testObserver().observedValues

        every { setWallpaperUseCase.setWallpaper(any(), any()) } returns Completable.error(
            IllegalArgumentException()
        )

        every { setWallpaperByBitmapUseCase.setWallpaperByBitmap(any()) } returns Completable.complete()

        viewModel.setWallpaper(uri, activity)
        verify(exactly = 1) {
            setWallpaperByBitmapUseCase.setWallpaperByBitmap(any())
        }
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `given setWallpaper is called, verify error when setWallpaper returns IllegalArgumentException and setWallpaperByBitmap errors`() {
        val observedValues = viewModel.uiState.testObserver().observedValues
        val errorMsg = "error"

        every { setWallpaperUseCase.setWallpaper(any(), any()) } returns Completable.error(
            IllegalArgumentException()
        )

        every { setWallpaperByBitmapUseCase.setWallpaperByBitmap(any()) } returns Completable.error(Throwable(errorMsg))

        viewModel.setWallpaper(uri, activity)
        verify(exactly = 1) {
            setWallpaperByBitmapUseCase.setWallpaperByBitmap(any())
        }
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    companion object {
        private val getPhotoResponse = GetPhotoResponse(
            "id",
            "created_at",
            "updated_at",
            720,
            1250,
            "#FFFFFF",
            69,
            69,
            69,
            false,
            "desc",
            Exif(),
            Location("city", "country"),
            listOf(Tag("shit"), Tag("garbage")),
            Urls("raw", "full", "regular", "small", "thumb"),
            Links(),
            User("user_id", "name", null)
        )
        private const val quality = "full"
        private const val fileName = "fileName"
        private const val id = "id"
    }
}
