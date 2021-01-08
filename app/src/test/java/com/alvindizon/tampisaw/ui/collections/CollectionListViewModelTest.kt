package com.alvindizon.tampisaw.ui.collections

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.GetAllCollectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class CollectionListViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getAllCollectionsUseCase: GetAllCollectionsUseCase

    private lateinit var SUT: CollectionListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = CollectionListViewModel(getAllCollectionsUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = SUT.uiState?.testObserver()

        every { getAllCollectionsUseCase.getAllCollections() } returns Observable.just(TestConstants.collectionsPagingData)

        SUT.getAllCollections()

        runBlocking {
            val collectionsList = uiState?.observedValues?.get(0)?.collectData()
            assertEquals(TestConstants.unsplashCollection, collectionsList?.get(0))
            assertEquals(TestConstants.unsplashCollection2, collectionsList?.get(1))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = SUT.uiState?.testObserver()

        every { getAllCollectionsUseCase.getAllCollections() } returns Observable.error(IOException())

        SUT.getAllCollections()

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }
}