package com.alvindizon.tampisaw.features.search

import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import com.alvindizon.tampisaw.domain.SearchCollectionsUseCase
import com.alvindizon.tampisaw.domain.SearchPhotosUseCase
import com.alvindizon.tampisaw.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException


@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class SearchViewModelTest {

    @MockK
    lateinit var searchPhotosUseCase: SearchPhotosUseCase

    @MockK
    lateinit var searchCollectionsUseCase: SearchCollectionsUseCase

    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(searchPhotosUseCase, searchCollectionsUseCase)
    }

    @Test
    fun `verify that usecases are called and correct query passed to usecase functions on updateQuery`() {
        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.just(TestConstants.photoPagingData)
        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.just(
            TestConstants.collectionsPagingData
        )
        val querySlots = mutableListOf<String>()

        viewModel.updateQuery(testQuery)

        verify(exactly = 1) { searchPhotosUseCase.searchPhotos(capture(querySlots)) }
        verify(exactly = 1) { searchCollectionsUseCase.searchCollections(capture(querySlots)) }

        assertEquals(querySlots[0], testQuery)
        assertEquals(querySlots[1], testQuery)
    }

    @Test
    fun `searchCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = viewModel.collections.testObserver()

        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.just(
            TestConstants.collectionsPagingData
        )

        viewModel.searchCollections(testQuery)

        runBlocking {
            val collectionsList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.unsplashCollection, collectionsList?.get(0))
            assertEquals(TestConstants.unsplashCollection2, collectionsList?.get(1))
        }
    }

    @Test
    fun `searchPhotos loads correct PagingData of type UnsplashPhoto`() {
        val uiState = viewModel.photos.testObserver()

        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.just(TestConstants.photoPagingData)

        viewModel.searchPhotos(testQuery)

        runBlocking {
            val photoList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList?.get(0))
            assertEquals(TestConstants.unsplashPhoto2, photoList?.get(1))
        }
    }

    @Test
    fun `empty paging data if error is encountered on getAllPhotos`() {
        val uiState = viewModel.photos.testObserver()

        every { searchPhotosUseCase.searchPhotos(testQuery) } returns Observable.error(IOException())

        viewModel.searchPhotos(testQuery)

        uiState.observedValues.isEmpty().let { assert(it) }
    }

    @Test
    fun `empty paging data if error is encountered on getAllCollections`() {
        val uiState = viewModel.collections.testObserver()

        every { searchCollectionsUseCase.searchCollections(testQuery) } returns Observable.error(
            IOException()
        )

        viewModel.searchCollections(testQuery)

        uiState.observedValues.isEmpty().let { assert(it) }
    }

    companion object {
        private const val testQuery = "doge"
    }
}
