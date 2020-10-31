package com.alvindizon.tampisaw.ui.collections

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.GetCollectionPhotosUseCase
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException

class CollectionViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock lateinit var getCollectionPhotoUseCase: GetCollectionPhotosUseCase

    private lateinit var SUT: CollectionViewModel

    @Before
    fun setUp() {
        SUT = CollectionViewModel(getCollectionPhotoUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllPhotos load correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.uiState?.testObserver()

        `when`(getCollectionPhotoUseCase.getCollectionPhotos(testId)).thenReturn(Observable.just(TestConstants.photoPagingData))

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

        `when`(getCollectionPhotoUseCase.getCollectionPhotos(testId)).thenReturn(Observable.error(IOException()))

        SUT.getAllPhotos(testId)

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }

    companion object {
        private const val testId = 123
    }
}