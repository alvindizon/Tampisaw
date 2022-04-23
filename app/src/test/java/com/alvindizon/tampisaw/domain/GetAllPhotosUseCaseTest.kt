package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.testbase.collectData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetAllPhotosUseCaseTest {

    private val unsplashRepo: UnsplashRepo = mockk()

    private lateinit var useCase: GetAllPhotosUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetAllPhotosUseCase(unsplashRepo)
    }

    @Test
    fun `verify usecase returns correct data when repo succeeds`() {
        every { unsplashRepo.getAllPhotos() } returns Observable.just(TestConstants.listPhotosPagingData)

        val pagingData = useCase.getAllPhotos().test().values()[0]

        runBlocking {
            val list = pagingData.collectData()
            assertEquals(TestConstants.unsplashPhoto, list[0])
            assertEquals(TestConstants.unsplashPhoto2, list[1])
        }
    }

    @Test
    fun `verify usecase errors when repo errors`() {
        val error = Throwable("error")
        every { unsplashRepo.getAllPhotos() } returns Observable.error(error)

        useCase.getAllPhotos().test().assertError {
            it.message == error.message
        }
    }
}
