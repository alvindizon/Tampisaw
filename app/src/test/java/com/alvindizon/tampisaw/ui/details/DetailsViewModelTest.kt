package com.alvindizon.tampisaw.ui.details

import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.data.networking.model.getphoto.*
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

    private lateinit var SUT: DetailsViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = DetailsViewModel(getPhotoUseCase)
    }

    @Test
    fun `observe correct states when getPhotos is successful`() {
        val uiStatus = SUT.uiState.testObserver()

        every { getPhotoUseCase.getPhoto(any()) } returns Single.just(getPhotoResponse)

        SUT.getPhoto("ID")
        val observedValues = uiStatus.observedValues
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], GetDetailSuccess(getPhotoResponse.toPhotoDetails()))
    }

    @Test
    fun `observe correct states when getPhotos is not successful`() {
        val uiStatus = SUT.uiState.testObserver()

        every { getPhotoUseCase.getPhoto(any()) } returns Single.error(IOException("erreur"))

        SUT.getPhoto("ID")
        val observedValues = uiStatus.observedValues
        assertEquals(observedValues[0], Loading)
        assertEquals(observedValues[1], Error("erreur"))
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
            Location("city", "country", Position(6.0, 7.0)),
            listOf(Tag("shit"), Tag("garbage")),
            null,
            Urls("raw", "full", "regular", "small", "thumb"),
            Links(),
            User(
                "user_id",
                "updated-at",
                "username",
                "name",
                null,
                null,
                "location",
                69,
                69,
                69,
                UserLinks("self", "html", "photos", "likes", "portfolio"),
                null
            )
        )

    }
}