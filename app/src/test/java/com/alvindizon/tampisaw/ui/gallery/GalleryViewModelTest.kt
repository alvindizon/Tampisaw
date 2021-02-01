package com.alvindizon.tampisaw.ui.gallery

import com.alvindizon.tampisaw.*
import com.alvindizon.tampisaw.domain.GetAllPhotosUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class, CoroutineExtension::class])
class GalleryViewModelTest {

    @MockK
    lateinit var getAllPhotosUseCase: GetAllPhotosUseCase

    private lateinit var SUT: GalleryViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = GalleryViewModel(getAllPhotosUseCase)
    }

    @Test
    fun `getAllPhotos loads correct PagingData of type UnsplashPhoto`() {
        val uiState = SUT.uiState.testObserver()

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.just(TestConstants.photoPagingData)

        SUT.getAllPhotos()

        runBlocking {
            val pagingData = uiState.observedValues[0] as GalleryUIState.DataLoaded
            val photoList = pagingData.pagingData.collectData()
            assertEquals(TestConstants.unsplashPhoto, photoList[0])
            assertEquals(TestConstants.unsplashPhoto2, photoList[1])
        }
    }

    @Test
    fun `empty paging data if error is encountered`() {
        val uiState = SUT.uiState.testObserver()

        every { getAllPhotosUseCase.getAllPhotos() } returns Observable.error(IOException())

        SUT.getAllPhotos()

        val errorState = uiState.observedValues[0] as GalleryUIState.Error
        assertEquals("error", errorState.message)
    }
}