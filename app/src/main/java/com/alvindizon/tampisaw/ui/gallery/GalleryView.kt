package com.alvindizon.tampisaw.ui.gallery

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData

typealias LoadStateListener = (CombinedLoadStates) -> Unit

sealed class GalleryUIState {
    object Loading : GalleryUIState()
    object Retry : GalleryUIState()
    object Refresh : GalleryUIState()
    object GalleryVisible : GalleryUIState()
    data class DataLoaded(var pagingData: PagingData<UnsplashPhoto>) : GalleryUIState()
    data class Error(var message: String) : GalleryUIState()
}

interface GalleryView {
    fun render(state: GalleryUIState) {
        when (state) {
            is GalleryUIState.DataLoaded -> dataLoaded(state.pagingData)
            GalleryUIState.GalleryVisible -> showGallery()
            is GalleryUIState.Error -> showError(state.message)
            GalleryUIState.Retry -> showRetry()
            GalleryUIState.Refresh -> showRefresh()
            GalleryUIState.Loading -> showLoading()
        }
    }

    fun showLoading() {
        showProgressBar(true)
        showList(false)
        showRetryButton(false)
    }

    fun showRefresh() {
        setSwipeLayoutState(true)
        loadPhotos()
    }

    fun showRetry() {
        showProgressBar(false)
        showList(false)
        showRetryButton(true)
    }

    fun showError(message: String) {
        setSwipeLayoutState(false)
        showErrorMessage(message)
    }

    fun showGallery() {
        showRetryButton(false)
        showProgressBar(false)
        setSwipeLayoutState(false)
        showList(true)
    }

    fun dataLoaded(pagingData: PagingData<UnsplashPhoto>)
    fun showList(isListVisible: Boolean)
    fun setSwipeLayoutState(isRefreshing: Boolean)
    fun showProgressBar(isProgressBarVisible: Boolean)
    fun showRetryButton(isRetryBtnVisible: Boolean)
    fun showErrorMessage(message: String)
    fun loadPhotos()

}