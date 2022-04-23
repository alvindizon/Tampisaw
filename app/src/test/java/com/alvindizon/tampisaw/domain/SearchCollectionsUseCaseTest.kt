package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.testbase.collectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SearchCollectionsUseCaseTest {

    private val unsplashRepo: UnsplashRepo = mockk()

    private lateinit var useCase: SearchCollectionsUseCase

    @BeforeEach
    fun setUp() {
        useCase = SearchCollectionsUseCase(unsplashRepo)
    }

    @Test
    fun `verify that usecase returns passes correct query to repo and returns correct data when repo succeeds`() {
        every {
            unsplashRepo.searchCollections(any())
        } returns Observable.just(TestConstants.collectionsResponsePagingData)

        val pagingData = useCase.searchCollections("eni").test().values()[0]

        val querySlot = slot<String>()

        verify(exactly = 1) { unsplashRepo.searchCollections(capture(querySlot)) }

        assertEquals("eni", querySlot.captured)

        runTest {
            val list = pagingData.collectData()
            assertEquals(TestConstants.unsplashCollection, list[0])
            assertEquals(TestConstants.unsplashCollection2, list[1])
        }
    }

    @Test
    fun `verify that if repo errors, usecase returns error`() {
        val error = Throwable("error")
        every { unsplashRepo.searchCollections(any()) } returns Observable.error(error)

        useCase.searchCollections("eni").test().assertError {
            it.message == error.message
        }
    }
}
