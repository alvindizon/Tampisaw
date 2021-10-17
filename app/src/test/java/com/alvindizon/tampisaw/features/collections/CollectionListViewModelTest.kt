package com.alvindizon.tampisaw.features.collections

import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import com.alvindizon.tampisaw.domain.GetAllCollectionsUseCase
import com.alvindizon.tampisaw.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class CollectionListViewModelTest {

    @MockK
    lateinit var getAllCollectionsUseCase: GetAllCollectionsUseCase

    private lateinit var SUT: CollectionListViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = CollectionListViewModel(getAllCollectionsUseCase)
    }

    @Test
    fun `getAllCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = SUT.uiState.testObserver()

        every {
            getAllCollectionsUseCase.getAllCollections()
        } returns Observable.just(TestConstants.collectionsPagingData)

        SUT.getAllCollections()

        runBlockingTest {
            val collectionsList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.unsplashCollection, collectionsList?.get(0))
            assertEquals(TestConstants.unsplashCollection2, collectionsList?.get(1))
        }
    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = SUT.uiState.testObserver()

        every { getAllCollectionsUseCase.getAllCollections() } returns Observable.error(IOException())

        SUT.getAllCollections()

        uiState.observedValues.isEmpty().let { assert(it) }
    }
}
