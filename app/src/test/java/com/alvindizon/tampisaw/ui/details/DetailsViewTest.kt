package com.alvindizon.tampisaw.ui.details

import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DetailsViewTest {

    private lateinit var view: SpyableDetailsView

    @BeforeEach
    fun setUp() {
        // we use a spy instead of a mock so that we don't have to mock the return value for the methods in DetailsView via `every`
        // a spy means that we are using the class' "actual" methods
        view = spyk()
    }

    @Test
    fun `when DetailsUIState is LOADING then view should call showLoading with true parameter`() {
        val detailsUIState = DetailsUIState.LOADING

        view.render(detailsUIState)

        val booleanSlot = slot<Boolean>()

        verify(exactly = 1) { view.showLoading(capture(booleanSlot)) }

        assertTrue(booleanSlot.captured)
    }

    @Test
    fun `when DetailsUIState is SUCCESS then view should call showSuccess with correct PhotoDetails as parameter`() {
        val photoDetails = PhotoDetails(
            "id",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "camera",
            "location",
            null,
            null,
            "raw",
            "full",
            "reg",
            "small",
            "thumb",
            null,
            null,
            null
        )
        val detailsUIState = DetailsUIState.SUCCESS(photoDetails)

        view.render(detailsUIState)

        val detailsSlot = mutableListOf<PhotoDetails>()

        verify(exactly = 1) { view.showSuccess(capture(detailsSlot)) }
        verify(exactly = 1) { view.loadImage(capture(detailsSlot)) }
        verify(exactly = 1) { view.setupFabOptions(capture(detailsSlot)) }
        verify(exactly = 1) { view.setupToolbar(capture(detailsSlot)) }

        assertEquals(photoDetails, detailsSlot[0])
        assertEquals(photoDetails, detailsSlot[1])
        assertEquals(photoDetails, detailsSlot[2])
        assertEquals(photoDetails, detailsSlot[3])
    }

    @Test
    fun `when DetailsUiState is ERROR then view should call showError`() {

        val detailsUIState = DetailsUIState.ERROR("error message boo!")

        view.render(detailsUIState)

        val errorMsgSlot = slot<String>()
        val booleanSlot = slot<Boolean>()

        verify(exactly = 1) { view.showError(capture(errorMsgSlot)) }
        verify(exactly = 1) { view.showLoading(capture(booleanSlot)) }

        assertEquals("error message boo!", errorMsgSlot.captured)
        assertFalse(booleanSlot.captured)
    }
}

class SpyableDetailsView : DetailsView {

    override fun showLoading(isVisible: Boolean) {
    }

    override fun setupToolbar(photoDetails: PhotoDetails) {
    }

    override fun setupFabOptions(photoDetails: PhotoDetails) {
    }

    override fun loadImage(photoDetails: PhotoDetails) {
    }

    override fun showError(errorMessage: String) {
    }
}
