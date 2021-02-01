package com.alvindizon.tampisaw.ui.gallery

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData

typealias LoadStateListener = (CombinedLoadStates) -> Unit

sealed class GalleryUIState {
    object Loading : GalleryUIState()
    object Retry : GalleryUIState()
    object Refresh : GalleryUIState()
    object GalleryVisible: GalleryUIState()
    data class DataLoaded(var pagingData: PagingData<UnsplashPhoto>) : GalleryUIState()
    data class Error(var message: String) : GalleryUIState()
}
interface GalleryView {

}