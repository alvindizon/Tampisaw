package com.alvindizon.tampisaw.details.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvindizon.tampisaw.core.ui.BaseViewModel
import com.alvindizon.tampisaw.details.model.PhotoDetails
import com.alvindizon.tampisaw.details.ui.DetailsUIState
import com.alvindizon.tampisaw.details.ui.DownloadSuccess
import com.alvindizon.tampisaw.details.ui.Downloading
import com.alvindizon.tampisaw.details.ui.DetailsError
import com.alvindizon.tampisaw.details.ui.GetDetailSuccess
import com.alvindizon.tampisaw.details.ui.Loading
import com.alvindizon.tampisaw.details.ui.SetWallpaperSuccess
import com.alvindizon.tampisaw.details.ui.SettingWallpaper
import com.alvindizon.tampisaw.details.usecase.DownloadPhotoUseCase
import com.alvindizon.tampisaw.details.usecase.GetPhotoUseCase
import com.alvindizon.tampisaw.details.usecase.SetWallpaperByBitmapUseCase
import com.alvindizon.tampisaw.details.usecase.SetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val downloadPhotoUseCase: DownloadPhotoUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val setWallpaperByBitmapUseCase: SetWallpaperByBitmapUseCase
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
                    _uiState.value = GetDetailSuccess(it)
                    photoDetails.set(it)
                },
                onError = { error ->
                    error.printStackTrace()
                    _uiState.value = error.message?.let {
                        DetailsError(it)
                    }
                }
            )
    }

    fun downloadPhoto(
        quality: String,
        fileName: String,
        id: String,
        activity: Activity,
        lifecycleOwner: LifecycleOwner
    ) {
        compositeDisposable += downloadPhotoUseCase.downloadPhoto(
            quality,
            fileName,
            id,
            activity,
            lifecycleOwner
        )
            .ignoreElement()
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
                        DetailsError(it)
                    }
                }
            )
    }

    fun downloadAndSetWallpaper(
        quality: String,
        fileName: String,
        id: String,
        activity: Activity,
        lifecycleOwner: LifecycleOwner
    ) {
        compositeDisposable += downloadPhotoUseCase.downloadPhoto(
            quality,
            fileName,
            id,
            activity,
            lifecycleOwner
        )
            .flatMapCompletable { uri ->
                setWallpaperUseCase.setWallpaper(uri, activity).onErrorResumeNext {
                    if (it is IllegalArgumentException) {
                        setWallpaperByBitmapUseCase.setWallpaperByBitmap(uri)
                    } else {
                        Completable.error(it)
                    }
                }
            }
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
                        DetailsError(it)
                    }
                }
            )
    }

    fun setWallpaper(uri: Uri, activity: Activity) {
        compositeDisposable += setWallpaperUseCase.setWallpaper(uri, activity)
            .onErrorResumeNext {
                if (it is IllegalArgumentException) {
                    setWallpaperByBitmapUseCase.setWallpaperByBitmap(uri)
                } else {
                    Completable.error(it)
                }
            }
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
                        DetailsError(it)
                    }
                }
            )
    }
}
