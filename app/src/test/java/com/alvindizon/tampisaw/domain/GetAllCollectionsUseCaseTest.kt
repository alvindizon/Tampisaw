package com.alvindizon.tampisaw.domain

import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetAllCollectionsUseCaseTest {

    private val unsplashRepo: UnsplashRepo = mockk()

    private lateinit var useCase: GetAllCollectionsUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetAllCollectionsUseCase(unsplashRepo)
    }

    @Test
    fun `verify that if repo returns success, usecase maps response correctly`() {
        every { unsplashRepo.getAllCollections() } returns Observable.just(TestConstants.collectionsResponsePagingData)

        val pagingData = useCase.getAllCollections().test().values()[0]

        runBlockingTest {
            val list = pagingData.collectData()
            assertEquals(TestConstants.unsplashCollection, list[0])
            assertEquals(TestConstants.unsplashCollection2, list[1])
        }
    }

    @Test
    fun `verify that if repo errors, usecase returns error`() {
        val error = Throwable("error")
        every { unsplashRepo.getAllCollections() } returns Observable.error(error)

        useCase.getAllCollections().test().assertError {
            it.message == error.message
        }
    }
}
