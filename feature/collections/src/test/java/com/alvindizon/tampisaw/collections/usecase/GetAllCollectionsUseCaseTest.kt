package com.alvindizon.tampisaw.collections.usecase

import com.alvindizon.tampisaw.collections.TestConstants
import com.alvindizon.tampisaw.collections.integration.CollectionsViewRepository
import com.alvindizon.tampisaw.testbase.collectData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetAllCollectionsUseCaseTest {

    private val repo: CollectionsViewRepository = mockk()

    private lateinit var useCase: GetAllCollectionsUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetAllCollectionsUseCase(repo)
    }

    @Test
    fun `verify that if repo returns success, usecase maps response correctly`() {
        every { repo.getAllCollections() } returns Observable.just(TestConstants.getAllCollectionsPagingData)

        val pagingData = useCase.getAllCollections().test().values()[0]

        runBlocking {
            val list = pagingData.collectData()
            assertEquals(TestConstants.collections, list[0])
            assertEquals(TestConstants.collections2, list[1])
        }
    }

    @Test
    fun `verify that if repo errors, usecase returns error`() {
        val error = Throwable("error")
        every { repo.getAllCollections() } returns Observable.error(error)

        useCase.getAllCollections().test().assertError {
            it.message == error.message
        }
    }
}
