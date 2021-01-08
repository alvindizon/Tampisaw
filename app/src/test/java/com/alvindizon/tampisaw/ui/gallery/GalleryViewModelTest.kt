package com.alvindizon.tampisaw.ui.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.GetAllPhotosUseCase
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

class GalleryViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getAllPhotosUseCase: GetAllPhotosUseCase

    private lateinit var SUT: GalleryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = GalleryViewModel(getAllPhotosUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getAllPhotos loads correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.uiState?.testObserver()

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.just(TestConstants.photoPagingData)

        SUT.getAllPhotos()

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

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.error(IOException())

        SUT.getAllPhotos()

        uiState?.observedValues?.isEmpty()?.let { assert(it) }
    }
}