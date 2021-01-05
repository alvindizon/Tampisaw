package com.alvindizon.tampisaw.core.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment {

    constructor(layoutResId: Int) : super(layoutResId)

    private val presentationComponent by lazy {
        (requireActivity() as BaseActivity).activityComponent.presentationComponent()
    }

    protected val injector
        get() = presentationComponent
}
