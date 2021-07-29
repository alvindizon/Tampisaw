package com.alvindizon.tampisaw.features.details

import android.content.Context
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvindizon.tampisaw.core.toPhotoDetails
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.domain.DownloadPhotoUseCase
import com.alvindizon.tampisaw.domain.GetPhotoUseCase
import com.alvindizon.tampisaw.domain.SetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val downloadPhotoUseCase: DownloadPhotoUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<DetailsUIState>()
    val uiState: LiveData<DetailsUIState> get() = _uiState

    val photoDetails = ObservableField<PhotoDetails>()

    fun getPhoto(id: String) {
        compositeDisposable += getPhotoUseCase.getPhoto(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = Loading }
            .subscribeBy(
                onSuccess = {
                    _uiState.value = GetDetailSuccess(it.toPhotoDetails())
                    photoDetails.set(it.toPhotoDetails())
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        Error(it)
                    }
                }
            )
    }

    fun downloadPhoto(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        compositeDisposable += downloadPhotoUseCase.downloadPhoto(
            quality,
            fileName,
            id,
            context,
            lifecycleOwner
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = Downloading }
            .subscribeBy(
                onComplete = {
                    _uiState.value = DownloadSuccess
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        Error(it)
                    }
                }
            )
    }

    fun downloadAndSetWallpaper(
        quality: String,
        fileName: String,
        id: String,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        compositeDisposable += setWallpaperUseCase.downloadAndSetWallpaper(
            quality,
            fileName,
            id,
            context,
            lifecycleOwner
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = SettingWallpaper }
            .subscribeBy(
                onComplete = {
                    _uiState.value = SetWallpaperSuccess
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        Error(it)
                    }
                }
            )
    }

    fun setWallpaper(uri: Uri) {
        compositeDisposable += setWallpaperUseCase.setWallpaperByUri(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _uiState.value = SettingWallpaper }
            .subscribeBy(
                onComplete = {
                    _uiState.value = SetWallpaperSuccess
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        Error(it)
                    }
                }
            )
    }
}
