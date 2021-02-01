package com.alvindizon.tampisaw.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.rxjava2.cachedIn
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.GetAllPhotosUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class GalleryViewModel @ViewModelInject constructor(
    private val getAllPhotosUseCase: GetAllPhotosUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<GalleryUIState>()
    val uiState: LiveData<GalleryUIState> get() = _uiState

    var isRefreshing: ObservableBoolean = ObservableBoolean()

    fun getAllPhotos() {
        compositeDisposable += getAllPhotosUseCase.getAllPhotos()
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    _uiState.value = GalleryUIState.DataLoaded(it)
                },
                onError = {
                    it.printStackTrace()
                    _uiState.value = GalleryUIState.Error(it.message ?: "error")
                }
            )
    }

    fun getLoadStateListener(): LoadStateListener {
        return { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                _uiState.value = GalleryUIState.GalleryVisible
            }
            if (loadState.source.refresh is LoadState.Loading && !isRefreshing.get()) {
                _uiState.value = GalleryUIState.Loading
            }
            if (loadState.source.refresh is LoadState.Error) {
                _uiState.value = GalleryUIState.Retry
            }

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                GalleryUIState.Error("\uD83D\uDE28 Wooops ${it.error}")
            }
        }
    }

    fun onLayoutRefresh() {
        _uiState.value = GalleryUIState.Refresh
    }

}