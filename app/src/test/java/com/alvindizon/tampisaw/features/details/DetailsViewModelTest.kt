package com.alvindizon.tampisaw.features.details

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.data.networking.model.getphoto.*
import com.alvindizon.tampisaw.domain.DownloadPhotoUseCase
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.domain.SetWallpaperUseCase
import com.alvindizon.tampisaw.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class DetailsViewModelTest {

    @MockK
    lateinit var getPhotoUseCase: GetPhotoUseCase

    @MockK
    lateinit var downloadPhotoUseCase: DownloadPhotoUseCase

    @MockK
    lateinit var setWallpaperUseCase: SetWallpaperUseCase

    private lateinit var SUT: DetailsViewModel

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var lifecycleOwner: LifecycleOwner

    @MockK
    lateinit var uri: Uri

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = DetailsViewModel(getPhotoUseCase, downloadPhotoUseCase, setWallpaperUseCase)
    }

    @Test
    fun `observe correct states when getPhotos is successful`() {
        val observedValues = SUT.uiState.testObserver().observedValues
        every { getPhotoUseCase.getPhoto(any()) } returns Single.just(getPhotoResponse)

        SUT.getPhoto("ID")
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], GetDetailSuccess(getPhotoResponse.toPhotoDetails()))
    }

    @Test
    fun `observe correct states when getPhotos is not successful`() {
        val observedValues = SUT.uiState.testObserver().observedValues
        val errorMsg = "error"

        every { getPhotoUseCase.getPhoto(any()) } returns Single.error(IOException(errorMsg))

        SUT.getPhoto("ID")
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when downloadPhotos is successful`() {
        val observedValues = SUT.uiState.testObserver().observedValues

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Completable.complete()

        SUT.downloadPhoto(quality, fileName, id, context, lifecycleOwner)
        assertEquals(observedValues[0], Downloading)
        assertEquals(observedValues[1], DownloadSuccess)
    }

    @Test
    fun `observe correct states when downloadPhotos errors`() {
        val observedValues = SUT.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            downloadPhotoUseCase.downloadPhoto(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Completable.error(Throwable(errorMsg))

        SUT.downloadPhoto(quality, fileName, id, context, lifecycleOwner)
        assertEquals(observedValues[0], Downloading)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when downloadAndSetWallpaper is successful`() {
        val observedValues = SUT.uiState.testObserver().observedValues

        every {
            setWallpaperUseCase.downloadAndSetWallpaper(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Completable.complete()

        SUT.downloadAndSetWallpaper(quality, fileName, id, context, lifecycleOwner)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `observe correct states when downloadAndSetWallpaper errors`() {
        val observedValues = SUT.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            setWallpaperUseCase.downloadAndSetWallpaper(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Completable.error(Throwable(errorMsg))

        SUT.downloadAndSetWallpaper(quality, fileName, id, context, lifecycleOwner)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], Error(errorMsg))
    }

    @Test
    fun `observe correct states when setWallpaper is successful`() {
        val observedValues = SUT.uiState.testObserver().observedValues

        every {
            setWallpaperUseCase.setWallpaperByUri(any())
        } returns Completable.complete()

        SUT.setWallpaper(uri)
        assertEquals(observedValues[0], SettingWallpaper)
        assertEquals(observedValues[1], SetWallpaperSuccess)
    }

    @Test
    fun `observe correct states when setWallpaper errors`() {
        val observedValues = SUT.uiState.testObserver().observedValues
        val errorMsg = "error"

        every {
            setWallpaperUseCase.setWallpaperByUri(any())
        } returns Completable.error(Throwable(errorMsg))

        SUT.setWallpaper(uri)
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
        private val quality = "full"
        private val fileName = "fileName"
        private val id = "id"
    }
}
