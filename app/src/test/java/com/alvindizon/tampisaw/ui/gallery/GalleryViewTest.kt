package com.alvindizon.tampisaw.ui.gallery

import androidx.paging.PagingData
import com.alvindizon.tampisaw.TestConstants.photoPagingData
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GalleryViewTest {
    private lateinit var view: SpyableGalleryView

    @BeforeEach
    fun setUp() {
        view = spyk()
    }

    @Test
    fun `during DataLoaded state dataLoaded() should be called with correct PagingData`() {
        val uiState = GalleryUIState.DataLoaded(photoPagingData)

        view.render(uiState)

        val pagingDataSlot = slot<PagingData<UnsplashPhoto>>()

        verify(exactly = 1) { view.dataLoaded(capture(pagingDataSlot)) }

        assertEquals(photoPagingData, pagingDataSlot.captured)
    }

    @Test
    fun `when ui state is GalleryVisible retry button and progress button are set to invisible, swipe layout is not refreshing and list is visible`() {
        val uiState = GalleryUIState.GalleryVisible

        view.render(uiState)

        val booleanSlotList = mutableListOf<Boolean>()

        verify(exactly = 1) { view.showGallery() }
        verify(exactly = 1) { view.showRetryButton(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showProgressBar(capture(booleanSlotList)) }
        verify(exactly = 1) { view.setSwipeLayoutState(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showList(capture(booleanSlotList)) }

        assertFalse(booleanSlotList[0])
        assertFalse(booleanSlotList[1])
        assertFalse(booleanSlotList[2])
        assertTrue(booleanSlotList[3])
    }

    @Test
    fun `when ui state is Error set swipeLayout refresh state to false and show snackbar`() {
        val uiState = GalleryUIState.Error("error message")

        view.render(uiState)

        val errorMsgSlot = slot<String>()
        val swipeLayoutRefreshSlot = slot<Boolean>()

        verify(exactly = 1) { view.showError(capture(errorMsgSlot)) }
        verify(exactly = 1) { view.setSwipeLayoutState(capture(swipeLayoutRefreshSlot)) }
        verify(exactly = 1) { view.showErrorMessage(capture(errorMsgSlot)) }

        assertEquals("error message", errorMsgSlot.captured)
        assertFalse(swipeLayoutRefreshSlot.captured)
    }

    @Test
    fun `when ui state is Retry, progress button and list should be invisible and retry should be visible`() {
        val uiState = GalleryUIState.Retry

        view.render(uiState)

        val booleanSlotList = mutableListOf<Boolean>()

        verify(exactly = 1) { view.showRetry() }
        verify(exactly = 1) { view.showProgressBar(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showList(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showRetryButton(capture(booleanSlotList)) }

        assertFalse(booleanSlotList[0])
        assertFalse(booleanSlotList[1])
        assertTrue(booleanSlotList[2])
    }

    @Test
    fun `when ui state is Refresh, swipeLayout should be set to refreshing and loadPhotos should be called`() {
        val uiState = GalleryUIState.Refresh

        view.render(uiState)

        val booleanSlot = slot<Boolean>()
        verify(exactly = 1) { view.showRefresh() }
        verify(exactly = 1) { view.setSwipeLayoutState(capture(booleanSlot)) }
        verify(exactly = 1) { view.loadPhotos() }

        assertTrue(booleanSlot.captured)
    }

    @Test
    fun `when ui state is loading show progress bar and make list and retry button invisible`() {
        val uiState = GalleryUIState.Loading

        view.render(uiState)

        val booleanSlotList = mutableListOf<Boolean>()

        verify(exactly = 1) { view.showLoading() }
        verify(exactly = 1) { view.showProgressBar(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showList(capture(booleanSlotList)) }
        verify(exactly = 1) { view.showRetryButton(capture(booleanSlotList)) }

        assertTrue(booleanSlotList[0])
        assertFalse(booleanSlotList[1])
        assertFalse(booleanSlotList[2])
    }
}

class SpyableGalleryView : GalleryView {
    override fun dataLoaded(pagingData: PagingData<UnsplashPhoto>) {
    }

    override fun showList(isListVisible: Boolean) {
    }

    override fun setSwipeLayoutState(isRefreshing: Boolean) {
    }

    override fun showProgressBar(isProgressBarVisible: Boolean) {
    }

    override fun showRetryButton(isRetryBtnVisible: Boolean) {
    }

    override fun showErrorMessage(message: String) {
    }

    override fun loadPhotos() {
    }

    override fun showInitialState() {
    }
}
