package com.alvindizon.tampisaw.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.MainCoroutineRule
import com.alvindizon.tampisaw.RxSchedulerRule
import com.alvindizon.tampisaw.any
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.data.networking.model.getphoto.*
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.testObserver
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

class DetailsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock lateinit var getPhotoUseCase: GetPhotoUseCase

    private lateinit var SUT: DetailsViewModel

    @Before
    fun setUp() {
        SUT = DetailsViewModel(getPhotoUseCase)
    }

    @Test
    fun `observe correct states when getPhotos is successful`() {
        val uiStatus = SUT.uiState?.testObserver()

        `when`(getPhotoUseCase.getPhoto(any())).thenReturn(Single.just(getPhotoResponse))

        SUT.getPhoto("ID")
        val observedValues = uiStatus?.observedValues
        assertEquals(observedValues?.get(0), LOADING)
        assertEquals(observedValues?.get(1), SUCCESS(getPhotoResponse.toPhotoDetails()))
    }

    @Test
    fun `observe correct states when getPhotos is not successful`() {
        val uiStatus = SUT.uiState?.testObserver()

        `when`(getPhotoUseCase.getPhoto(any())).thenReturn(Single.error(IOException("erreur")))

        SUT.getPhoto("ID")
        val observedValues = uiStatus?.observedValues
        assertEquals(observedValues?.get(0), LOADING)
        assertEquals(observedValues?.get(1), ERROR("erreur"))
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
            User("user_id", "updated-at", "username", "name", null, null, "location", 69, 69, 69, UserLinks("self", "html", "photos", "likes", "portfolio"), null)
        )

    }
}