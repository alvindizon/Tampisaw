package com.alvindizon.tampisaw.ui.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.GetCollectionPhotosUseCase
import com.alvindizon.tampisaw.ui.gallery.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionPhotosUseCase: GetCollectionPhotosUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<PagingData<UnsplashPhoto>>()
    val uiState: LiveData<PagingData<UnsplashPhoto>> get() = _uiState

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