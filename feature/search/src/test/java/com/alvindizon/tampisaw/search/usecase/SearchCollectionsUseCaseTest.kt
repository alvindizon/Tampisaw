package com.alvindizon.tampisaw.search.usecase

import com.alvindizon.tampisaw.search.TestConstants
import com.alvindizon.tampisaw.search.integration.SearchViewRepository
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

    private val repo: SearchViewRepository = mockk()

    private lateinit var useCase: SearchCollectionsUseCase

    @BeforeEach
    fun setUp() {
        useCase = SearchCollectionsUseCase(repo)
    }

    @Test
    fun `verify that usecase returns passes correct query to repo and returns correct data when repo succeeds`() {
        every {
            repo.searchCollections(any())
        } returns Observable.just(TestConstants.collectionsResponsePagingData)

        val pagingData = useCase.searchCollections("eni").test().values()[0]

        val querySlot = slot<String>()

        verify(exactly = 1) { repo.searchCollections(capture(querySlot)) }

        assertEquals("eni", querySlot.captured)

        runTest {
            val list = pagingData.collectData()
            assertEquals(TestConstants.collections, list[0])
            assertEquals(TestConstants.collections2, list[1])
        }
    }

    @Test
    fun `verify that if repo errors, usecase returns error`() {
        val error = Throwable("error")
        every { repo.searchCollections(any()) } returns Observable.error(error)

        useCase.searchCollections("eni").test().assertError {
            it.message == error.message
        }
    }
}
