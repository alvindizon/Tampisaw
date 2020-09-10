package com.alvindizon.tampisaw.ui.details

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(private val getPhotoUseCase: GetPhotoUseCase): BaseViewModel() {

    private val _uiState = MutableLiveData<DetailsUIState>()
    val uiState: LiveData<DetailsUIState>? get() = _uiState

    val photoDetails = ObservableField<PhotoDetails>()

    fun getPhoto(id: String) {
        compositeDisposable += getPhotoUseCase.getPhoto(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = LOADING }
            .subscribeBy(
                onSuccess = {
                    _uiState.value = SUCCESS(it.toPhotoDetails())
                    photoDetails.set(it.toPhotoDetails())
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        ERROR(it)
                    }
                }
            )
    }

}