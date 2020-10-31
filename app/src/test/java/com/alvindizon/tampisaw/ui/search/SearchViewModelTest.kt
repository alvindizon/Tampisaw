package com.alvindizon.tampisaw.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.SearchCollectionsUseCase
import com.alvindizon.tampisaw.domain.SearchPhotosUseCase
import com.nhaarman.mockitokotlin2.argumentCaptor
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

class SearchViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock lateinit var searchPhotosUseCase: SearchPhotosUseCase

    @Mock lateinit var searchCollectionsUseCase: SearchCollectionsUseCase

    private lateinit var SUT: SearchViewModel

    @Before
    fun setUp() {
        SUT = SearchViewModel(searchPhotosUseCase, searchCollectionsUseCase)
    }

    @Test
    fun `correct query passed to usecase functions`() {
        `when`(searchPhotosUseCase.searchPhotos(testQuery)).thenReturn(Observable.just(TestConstants.photoPagingData))
        `when`(searchCollectionsUseCase.searchCollections(testQuery)).thenReturn(Observable.just(TestConstants.collectionsPagingData))
        val ac = argumentCaptor<String>()

        SUT.updateQuery(testQuery)

        verify(searchPhotosUseCase, times(1)).searchPhotos(ac.capture())
        verify(searchCollectionsUseCase, times(1)).searchCollections(ac.capture())

        val captures = ac.allValues
        assertEquals(captures[0], testQuery)
        assertEquals(captures[1], testQuery)
    }

    @Test
    fun `when updateQuery is called searchPhotos and searchCollections are called`() {
        `when`(searchPhotosUseCase.searchPhotos(testQuery)).thenReturn(Observable.just(TestConstants.photoPagingData))
        `when`(searchCollectionsUseCase.searchCollections(testQuery)).thenReturn(Observable.just(TestConstants.collectionsPagingData))

        SUT.updateQuery(testQuery)

        verify(searchPhotosUseCase, times(1)).searchPhotos(testQuery)
        verify(searchCollectionsUseCase, times(1)).searchCollections(testQuery)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `searchCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = SUT.collections?.testObserver()

        `when`(searchCollectionsUseCase.searchCollections(testQuery)).thenReturn(Observable.just(TestConstants.collectionsPagingData))

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

        `when`(searchPhotosUseCase.searchPhotos(testQuery)).thenReturn(Observable.just(TestConstants.photoPagingData))

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

        `when`(searchPhotosUseCase.searchPhotos(testQuery)).thenReturn(Observable.error(IOException()))

        SUT.searchPhotos(testQuery)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `empty paging data if error is encountered on getAllCollections`() {
        val uiState = SUT.collections?.testObserver()

        `when`(searchCollectionsUseCase.searchCollections(testQuery)).thenReturn(Observable.error(IOException()))

        SUT.searchCollections(testQuery)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    companion object {
        private const val testQuery = "doge"
    }
}