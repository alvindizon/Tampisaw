package com.alvindizon.tampisaw.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.alvindizon.tampisaw.collections.model.Collection
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.gallery.model.Photo
import com.alvindizon.tampisaw.search.usecase.SearchCollectionsUseCase
import com.alvindizon.tampisaw.search.usecase.SearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase,
    private val searchCollectionsUseCase: SearchCollectionsUseCase
) : BaseViewModel() {

    private val _photos = MutableLiveData<PagingData<Photo>>()
    val photos: LiveData<PagingData<Photo>> get() = _photos

    private val _collections = MutableLiveData<PagingData<Collection>>()
    val collections: LiveData<PagingData<Collection>> get() = _collections

    fun updateQuery(query: String) {
        searchPhotos(query)
        searchCollections(query)
    }

    fun searchPhotos(query: String) {
        compositeDisposable += searchPhotosUseCase.searchPhotos(query)
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _photos.value = it },
                onError = { it.printStackTrace() }
            )
    }

    fun searchCollections(query: String) {
        compositeDisposable += searchCollectionsUseCase.searchCollections(query)
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _collections.value = it },
                onError = { it.printStackTrace() }
            )
    }
}
