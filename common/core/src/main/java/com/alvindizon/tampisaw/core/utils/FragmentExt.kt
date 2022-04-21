package com.alvindizon.tampisaw.core.utils

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator

fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
}

fun getNavigatorExtras(vararg views: View): FragmentNavigator.Extras {
    return FragmentNavigator.Extras.Builder().apply {
        views.forEach { addSharedElement(it, it.transitionName) }
    }.build()
}
