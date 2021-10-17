package com.alvindizon.tampisaw.core.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel  constructor(
    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()
): ViewModel(), DefaultLifecycleObserver{

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        compositeDisposable.clear()
    }
}
