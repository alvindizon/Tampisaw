package com.alvindizon.tampisaw.features.gallery

import com.alvindizon.tampisaw.CoroutineExtension
import com.alvindizon.tampisaw.InstantExecutorExtension
import com.alvindizon.tampisaw.RxSchedulerExtension
import com.alvindizon.tampisaw.TestConstants
import com.alvindizon.tampisaw.collectData
import com.alvindizon.tampisaw.domain.GetAllPhotosUseCase
import com.alvindizon.tampisaw.testObserver
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
class GalleryViewModelTest {

    @MockK
    lateinit var getAllPhotosUseCase: GetAllPhotosUseCase

    private lateinit var viewModel: GalleryViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = GalleryViewModel(getAllPhotosUseCase)
    }

    @Test
    fun `getAllPhotos loads correct PagingData of type UnsplashPhoto`() {
        val uiState = viewModel.uiState.testObserver()

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.just(TestConstants.photoPagingData)

        viewModel.getAllPhotos()

        runBlocking {
            val photoList = uiState.observedValues[0]?.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList?.get(0))
            assertEquals(TestConstants.unsplashPhoto2, photoList?.get(1))
        }
    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = viewModel.uiState.testObserver()

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.error(IOException())

        viewModel.getAllPhotos()

        uiState.observedValues.isEmpty().let { assert(it) }
    }
}
