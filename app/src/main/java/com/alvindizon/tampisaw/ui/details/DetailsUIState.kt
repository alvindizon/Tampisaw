package com.alvindizon.tampisaw.ui.details

sealed class DetailsUIState

object Loading : DetailsUIState()

data class GetDetailSuccess(val photoDetails: PhotoDetails) : DetailsUIState()

data class Error(val message: String) : DetailsUIState()

object Downloading: DetailsUIState()

object SettingWallpaper : DetailsUIState()

object DownloadSuccess: DetailsUIState()

object SetWallpaperSuccess: DetailsUIState()
