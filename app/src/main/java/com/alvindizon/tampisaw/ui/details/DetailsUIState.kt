package com.alvindizon.tampisaw.ui.details

sealed class DetailsUIState

object LOADING: DetailsUIState()

data class SUCCESS (
    var photoUrl: String
): DetailsUIState()

data class ERROR (
    var message: String
): DetailsUIState()