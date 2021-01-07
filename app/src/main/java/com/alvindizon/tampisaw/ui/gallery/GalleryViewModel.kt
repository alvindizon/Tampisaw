package com.alvindizon.tampisaw.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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

    private val _uiState = MutableLiveData<PagingData<UnsplashPhoto>>()
    val uiState: LiveData<PagingData<UnsplashPhoto>> get() = _uiState

    fun getAllPhotos() {
        compositeDisposable += getAllPhotosUseCase.getAllPhotos()
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _uiState.value = it },
                onError = {
                    it.printStackTrace()
                }
            )
    }

}