package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SearchPhotosUseCaseTest {

    private val unsplashRepo: UnsplashRepo = mockk()

    private lateinit var useCase: SearchPhotosUseCase

    @BeforeEach
    fun setUp() {
        useCase = SearchPhotosUseCase(unsplashRepo)
    }

    @Test
    fun `verify usecase passes query to repo and returns correct data when repo succeeds`() {
        every { unsplashRepo.searchPhotos(any()) } returns Observable.just(TestConstants.listPhotosPagingData)

        val pagingData = useCase.searchPhotos("eni").test().values()[0]

        val querySlot = slot<String>()

        verify(exactly = 1) { unsplashRepo.searchPhotos(capture(querySlot)) }

        assertEquals("eni", querySlot.captured)

        runBlocking {
            val list = pagingData.collectData()
            assertEquals(TestConstants.unsplashPhoto, list[0])
            assertEquals(TestConstants.unsplashPhoto2, list[1])
        }
    }

    @Test
    fun `verify usecase errors when repo errors`() {
        val error = Throwable("error")
        every { unsplashRepo.searchPhotos(any()) } returns Observable.error(error)

        useCase.searchPhotos("eni").test().assertError {
            it.message == error.message
        }
    }
}
