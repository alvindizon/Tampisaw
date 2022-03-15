package com.alvindizon.tampisaw.features.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.GetAllCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    private val getAllCollectionsUseCase: GetAllCollectionsUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<PagingData<UnsplashCollection>>()
    val uiState: LiveData<PagingData<UnsplashCollection>> get() = _uiState

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
