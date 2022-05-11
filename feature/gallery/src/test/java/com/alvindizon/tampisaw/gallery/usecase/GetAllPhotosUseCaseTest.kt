package com.alvindizon.tampisaw.gallery.usecase

import com.alvindizon.tampisaw.gallery.TestConstants
import com.alvindizon.tampisaw.gallery.integration.GalleryViewRepository
import com.alvindizon.tampisaw.testbase.collectData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetAllPhotosUseCaseTest {

    private val repo: GalleryViewRepository = mockk()

    private lateinit var useCase: GetAllPhotosUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetAllPhotosUseCase(repo)
    }

    @Test
    fun `verify usecase returns correct data when repo succeeds`() {
        every { repo.getAllPhotos() } returns Observable.just(TestConstants.getAllPhotosPagingData)

        val pagingData = useCase.getAllPhotos().test().values()[0]

        runBlocking {
            val list = pagingData.collectData()
            assertEquals(TestConstants.photo1, list[0])
            assertEquals(TestConstants.photo2, list[1])
        }
    }

    @Test
    fun `verify usecase errors when repo errors`() {
        val error = Throwable("error")
        every { repo.getAllPhotos() } returns Observable.error(error)

        useCase.getAllPhotos().test().assertError {
            it.message == error.message
        }
    }
}
