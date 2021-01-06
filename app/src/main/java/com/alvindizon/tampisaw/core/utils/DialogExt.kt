package com.alvindizon.tampisaw.core.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.alvindizon.tampisaw.core.Const.DIALOG_TAG


fun FragmentManager.dismissCurrentDialog() {
    val currentDialog = findFragmentByTag(DIALOG_TAG) as DialogFragment?
    currentDialog?.dismissAllowingStateLoss()
}

fun FragmentManager.showDialogFragment(dialogFragment: DialogFragment) {
    dismissCurrentDialog()
    dialogFragment.show(this, DIALOG_TAG)
}

