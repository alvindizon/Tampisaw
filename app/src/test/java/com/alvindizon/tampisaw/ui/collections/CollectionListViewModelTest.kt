package com.alvindizon.tampisaw.ui.collections

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.domain.GetAllCollectionsUseCase
import com.alvindizon.tampisaw.domain.GetCollectionPhotosUseCase
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

class CollectionListViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock lateinit var getAllCollectionsUseCase: GetAllCollectionsUseCase

    private lateinit var SUT: CollectionListViewModel

    @Before
    fun setUp() {
        SUT = CollectionListViewModel(getAllCollectionsUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = SUT.uiState?.testObserver()

        `when`(getAllCollectionsUseCase.getAllCollections()).thenReturn(Observable.just(TestConstants.collectionsPagingData))

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

        `when`(getAllCollectionsUseCase.getAllCollections()).thenReturn(Observable.error(
            IOException()
        ))

        SUT.getAllCollections()

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }
}