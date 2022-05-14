package com.alvindizon.tampisaw.collections.viewmodel

import com.alvindizon.tampisaw.collections.TestConstants
import com.alvindizon.tampisaw.collections.usecase.GetAllCollectionsUseCase
import com.alvindizon.tampisaw.testbase.CoroutineExtension
import com.alvindizon.tampisaw.testbase.InstantExecutorExtension
import com.alvindizon.tampisaw.testbase.RxSchedulerExtension
import com.alvindizon.tampisaw.testbase.collectData
import com.alvindizon.tampisaw.testbase.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class)
class CollectionListViewModelTest {

    @MockK
    lateinit var getAllCollectionsUseCase: GetAllCollectionsUseCase

    private lateinit var viewModel: CollectionListViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = CollectionListViewModel(getAllCollectionsUseCase)
    }

    @Test
    fun `getAllCollections loads correct PagingData of type UnsplashCollection`() {
        val uiState = viewModel.uiState.testObserver()

        every {
            getAllCollectionsUseCase.getAllCollections()
        } returns Observable.just(TestConstants.getAllCollectionsPagingData)

        viewModel.getAllCollections()

        runBlocking {
            val collectionsList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.collections, collectionsList?.get(0))
            assertEquals(TestConstants.collections2, collectionsList?.get(1))
        }
    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = viewModel.uiState.testObserver()

        every { getAllCollectionsUseCase.getAllCollections() } returns Observable.error(IOException())

        viewModel.getAllCollections()

        uiState.observedValues.isEmpty().let { assert(it) }
    }
}
