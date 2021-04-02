package com.alvindizon.tampisaw.ui.gallery

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData

typealias LoadStateListener = (CombinedLoadStates) -> Unit

sealed class GalleryUIState {
    data class Loading(val isVisible: Boolean) : GalleryUIState()
    data class Retry(val isVisible: Boolean) : GalleryUIState()
    object Refresh : GalleryUIState()
    data class GalleryVisible(val isVisible: Boolean) : GalleryUIState()
    data class DataLoaded(var pagingData: PagingData<UnsplashPhoto>) : GalleryUIState()
    data class Error(var message: String) : GalleryUIState()
}

interface GalleryView {
    fun render(state: GalleryUIState) {
        when (state) {
            is GalleryUIState.DataLoaded -> setData(state.pagingData)
            is GalleryUIState.GalleryVisible -> showList(state.isVisible)
            is GalleryUIState.Error -> showError(state.message)
            is GalleryUIState.Retry -> showRetryButton(state.isVisible)
            GalleryUIState.Refresh -> showRefresh()
            is GalleryUIState.Loading -> showProgressBar(state.isVisible)
        }
    }

    fun setData(pagingData: PagingData<UnsplashPhoto>) {
        setSwipeLayoutState(false)
        dataLoaded(pagingData)
    }

    fun showRefresh() {
        setSwipeLayoutState(true)
        loadPhotos()
    }

    fun showError(message: String) {
        setSwipeLayoutState(false)
        showErrorMessage(message)
    }

    fun dataLoaded(pagingData: PagingData<UnsplashPhoto>)
    fun showList(isListVisible: Boolean)
    fun setSwipeLayoutState(isRefreshing: Boolean)
    fun showProgressBar(isProgressBarVisible: Boolean)
    fun showRetryButton(isRetryBtnVisible: Boolean)
    fun showErrorMessage(message: String)
    fun loadPhotos()

}