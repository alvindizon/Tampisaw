package com.alvindizon.tampisaw.collections.usecase

import com.alvindizon.tampisaw.collections.TestConstants
import com.alvindizon.tampisaw.collections.integration.CollectionsViewRepository
import com.alvindizon.tampisaw.testbase.collectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetCollectionPhotosUseCaseTest {

    private val repo: CollectionsViewRepository = mockk()

    private lateinit var useCase: GetCollectionPhotosUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetCollectionPhotosUseCase(repo)
    }

    @Test
    fun `verify usecase passes query to repo and returns correct data when repo succeeds`() {
        every { repo.getCollectionPhotos(any()) } returns Observable.just(TestConstants.listPhotosPagingData)

        val pagingData = useCase.getCollectionPhotos("eni").test().values()[0]

        val querySlot = slot<String>()

        verify(exactly = 1) { repo.getCollectionPhotos(capture(querySlot)) }

        assertEquals("eni", querySlot.captured)

        runBlocking {
            val list = pagingData.collectData()
            assertEquals(TestConstants.photo1, list[0])
            assertEquals(TestConstants.photo2, list[1])
        }
    }

    @Test
    fun `verify usecase errors when repo errors`() {
        val error = Throwable("error")
        every { repo.getCollectionPhotos(any()) } returns Observable.error(error)

        useCase.getCollectionPhotos("eni").test().assertError {
            it.message == error.message
        }
    }
}
