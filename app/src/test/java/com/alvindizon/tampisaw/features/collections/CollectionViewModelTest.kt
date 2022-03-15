package com.alvindizon.tampisaw.features.collections

import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import com.alvindizon.tampisaw.domain.GetCollectionPhotosUseCase
import com.alvindizon.tampisaw.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException


@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class CollectionViewModelTest {

    @MockK
    lateinit var getCollectionPhotoUseCase: GetCollectionPhotosUseCase

    private lateinit var SUT: CollectionViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = CollectionViewModel(getCollectionPhotoUseCase)
    }

    @Test
    fun `getAllPhotos load correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.uiState.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.just(
            TestConstants.photoPagingData
        )

        SUT.getAllPhotos(testId)

        runBlockingTest {
            val photoList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList?.get(0))
            assertEquals(TestConstants.unsplashPhoto2, photoList?.get(1))
        }

    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = SUT.uiState.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.error(
            IOException()
        )

        SUT.getAllPhotos(testId)

        uiState.observedValues.isEmpty().let { assert(it) }
    }

    companion object {
        private const val testId = "123"
    }
}
