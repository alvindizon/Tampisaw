package com.alvindizon.tampisaw.ui.collections

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.GetCollectionPhotosUseCase
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

class CollectionViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getCollectionPhotoUseCase: GetCollectionPhotosUseCase

    private lateinit var SUT: CollectionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = CollectionViewModel(getCollectionPhotoUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllPhotos load correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.uiState?.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.just(
            TestConstants.photoPagingData
        )

        SUT.getAllPhotos(testId)

        runBlocking {
            val photoList = uiState?.observedValues?.get(0)?.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList?.get(0))
            assertEquals(TestConstants.unsplashPhoto2, photoList?.get(1))
        }

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = SUT.uiState?.testObserver()

        every { getCollectionPhotoUseCase.getCollectionPhotos(testId) } returns Observable.error(
            IOException()
        )

        SUT.getAllPhotos(testId)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    companion object {
        private const val testId = 123
    }
}