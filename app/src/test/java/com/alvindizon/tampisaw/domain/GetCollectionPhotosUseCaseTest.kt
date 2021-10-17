package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetCollectionPhotosUseCaseTest {

    private val unsplashRepo: UnsplashRepo = mockk()

    private lateinit var useCase: GetCollectionPhotosUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetCollectionPhotosUseCase(unsplashRepo)
    }

    @Test
    fun `verify usecase passes query to repo and returns correct data when repo succeeds`() {
        every { unsplashRepo.getCollectionPhotos(any()) } returns Observable.just(TestConstants.listPhotosPagingData)

        val pagingData = useCase.getCollectionPhotos("eni").test().values()[0]

        val querySlot = slot<String>()

        verify(exactly = 1) { unsplashRepo.getCollectionPhotos(capture(querySlot)) }

        assertEquals("eni", querySlot.captured)

        runBlockingTest {
            val list = pagingData.collectData()
            assertEquals(TestConstants.unsplashPhoto, list[0])
            assertEquals(TestConstants.unsplashPhoto2, list[1])
        }
    }

    @Test
    fun `verify usecase errors when repo errors`() {
        val error = Throwable("error")
        every { unsplashRepo.getCollectionPhotos(any()) } returns Observable.error(error)

        useCase.getCollectionPhotos("eni").test().assertError {
            it.message == error.message
        }
    }
}
