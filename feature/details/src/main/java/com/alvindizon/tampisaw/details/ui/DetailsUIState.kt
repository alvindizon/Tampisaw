package com.alvindizon.tampisaw.details.ui

import com.alvindizon.tampisaw.details.model.PhotoDetails

sealed class DetailsUIState

object Loading : DetailsUIState()

data class GetDetailSuccess(val photoDetails: PhotoDetails) : DetailsUIState()

data class DetailsError(val message: String) : DetailsUIState()

object Downloading: DetailsUIState()

object SettingWallpaper : DetailsUIState()

object DownloadSuccess: DetailsUIState()

object SetWallpaperSuccess: DetailsUIState()
