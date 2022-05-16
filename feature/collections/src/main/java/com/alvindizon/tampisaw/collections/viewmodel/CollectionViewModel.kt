package com.alvindizon.tampisaw.collections.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.alvindizon.tampisaw.collections.usecase.GetCollectionPhotosUseCase
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.gallery.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionPhotosUseCase: GetCollectionPhotosUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<PagingData<Photo>>()
    val uiState: LiveData<PagingData<Photo>> get() = _uiState

    fun getAllPhotos(id: String) {
        compositeDisposable += getCollectionPhotosUseCase.getCollectionPhotos(id)
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
