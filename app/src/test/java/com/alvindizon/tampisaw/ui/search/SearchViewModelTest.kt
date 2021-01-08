package com.alvindizon.tampisaw.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.SearchCollectionsUseCase
import com.alvindizon.tampisaw.domain.SearchPhotosUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SearchViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    lateinit var searchPhotosUseCase: SearchPhotosUseCase

    @MockK
    lateinit var searchCollectionsUseCase: SearchCollectionsUseCase

    private lateinit var SUT: SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = SearchViewModel(searchPhotosUseCase, searchCollectionsUseCase)
    }

    @Test
    fun `verify that usecases are called and correct query passed to usecase functions on updateQuery`() {
        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.just(TestConstants.photoPagingData)
        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.just(
            TestConstants.collectionsPagingData
        )
        val querySlots = mutableListOf<String>()

        SUT.updateQuery(testQuery)

        verify(exactly = 1) { searchPhotosUseCase.searchPhotos(capture(querySlots)) }
        verify(exactly = 1) { searchCollectionsUseCase.searchCollections(capture(querySlots)) }

        assertEquals(querySlots[0], testQuery)
        assertEquals(querySlots[1], testQuery)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `searchCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = SUT.collections?.testObserver()

        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.just(
            TestConstants.collectionsPagingData
        )

        SUT.searchCollections(testQuery)

        runBlocking {
            val collectionsList = uiState?.observedValues?.get(0)?.collectData()
            assertEquals(TestConstants.unsplashCollection, collectionsList?.get(0))
            assertEquals(TestConstants.unsplashCollection2, collectionsList?.get(1))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `searchPhotos loads correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.photos?.testObserver()

        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.just(TestConstants.photoPagingData)

        SUT.searchPhotos(testQuery)

        runBlocking {
            val photoList = uiState?.observedValues?.get(0)?.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList?.get(0))
            assertEquals(TestConstants.unsplashPhoto2, photoList?.get(1))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `empty paging data if error is encountered on getAllPhotos`() {
        val uiState = SUT.photos?.testObserver()

        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.error(IOException())

        SUT.searchPhotos(testQuery)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `empty paging data if error is encountered on getAllCollections`() {
        val uiState = SUT.collections?.testObserver()

        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.error(
            IOException()
        )

        SUT.searchCollections(testQuery)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    companion object {
        private const val testQuery = "doge"
    }
}