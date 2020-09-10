package com.alvindizon.tampisaw.ui.details

sealed class DetailsUIState

object LOADING: DetailsUIState()

data class SUCCESS (
    var photoDetails: PhotoDetails
): DetailsUIState()

data class ERROR (
    var message: String
): DetailsUIState()

object DOWNLOADING_IMG: DetailsUIState()

object IMAGE_SAVED: DetailsUIState()