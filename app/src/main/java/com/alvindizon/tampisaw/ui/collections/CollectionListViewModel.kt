package com.alvindizon.tampisaw.ui.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.GetAllCollectionsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CollectionListViewModel(private val getAllCollectionsUseCase: GetAllCollectionsUseCase): BaseViewModel() {

    private val _uiState = MutableLiveData<PagingData<UnsplashCollection>>()
    val uiState: LiveData<PagingData<UnsplashCollection>>? get() = _uiState

    fun getAllCollections() {
        compositeDisposable += getAllCollectionsUseCase.getAllCollections()
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