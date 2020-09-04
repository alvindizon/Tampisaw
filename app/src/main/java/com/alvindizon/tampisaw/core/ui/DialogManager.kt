package com.alvindizon.tampisaw.core.ui

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogManager @Inject constructor(private val fragmentManager: FragmentManager) {
    val currentlyShownDialog: DialogFragment?
        get() = fragmentManager.findFragmentByTag(DIALOG_TAG) as DialogFragment?

    fun dismissCurrentDialog() {
        val currentDialog = currentlyShownDialog
        currentDialog?.dismissAllowingStateLoss()
    }

    fun showDialog(fragment: DialogFragment) {
        dismissCurrentDialog()
        fragment.show(fragmentManager, DIALOG_TAG)
    }

    companion object {
        private val DIALOG_TAG = DialogManager::class.java.simpleName + ".DIALOG_TAG"
    }
}