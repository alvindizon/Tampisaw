package com.alvindizon.tampisaw.ui.details

sealed class DetailsUIState {
    object LOADING : DetailsUIState()
    data class SUCCESS(var photoDetails: PhotoDetails) : DetailsUIState()
    data class ERROR(var message: String) : DetailsUIState()
}


interface DetailsView {
    fun render(state: DetailsUIState) = when(state) {
        DetailsUIState.LOADING -> showLoading(true)
        is DetailsUIState.SUCCESS -> {
            showLoading(false)
            showSuccess(state.photoDetails)
        }
        is DetailsUIState.ERROR -> {
            showLoading(false)
            showError(state.message)
        }
    }

    fun showLoading(isVisible: Boolean)
    fun showSuccess(photoDetails: PhotoDetails) {
        loadImage(photoDetails)
        setupFabOptions(photoDetails)
        setupToolbar(photoDetails)
    }
    fun setupToolbar(photoDetails: PhotoDetails)
    fun setupFabOptions(photoDetails: PhotoDetails)
    fun loadImage(photoDetails: PhotoDetails)
    fun showError(errorMessage: String)
}