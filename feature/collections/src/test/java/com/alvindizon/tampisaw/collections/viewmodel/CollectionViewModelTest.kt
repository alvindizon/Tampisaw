package com.alvindizon.tampisaw.collections.viewmodel

import com.alvindizon.tampisaw.collections.TestConstants
import com.alvindizon.tampisaw.collections.usecase.GetCollectionPhotosUseCase
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


@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class CollectionViewModelTest {

    @MockK
    lateinit var getCollectionPhotoUseCase: GetCollectionPhotosUseCase

    private lateinit var viewModel: CollectionViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = CollectionViewModel(getCollectionPhotoUseCase)
    }

    @Test
    fun `getAllPhotos load correct PagingData of type UnsplashPhoto`() {
        val uiState = viewModel.uiState.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.just(
            TestConstants.listPhotosPagingData
        )

        viewModel.getAllPhotos(testId)

        runBlocking {
            val photoList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.photo1, photoList?.get(0))
            assertEquals(TestConstants.photo2, photoList?.get(1))
        }

    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = viewModel.uiState.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.error(
            IOException()
        )

        viewModel.getAllPhotos(testId)

        uiState.observedValues.isEmpty().let { assert(it) }
    }

    companion object {
        private const val testId = "123"
    }
}
