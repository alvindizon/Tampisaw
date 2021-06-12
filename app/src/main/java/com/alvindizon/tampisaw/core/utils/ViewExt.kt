package com.alvindizon.tampisaw.core.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSnackbar(
    message: String,
    duration: Int,
    buttonNameResId: Int? = null,
    actionTextColor: Int? = null,
    anchorView: View? = null,
    buttonAction: ((View) -> Unit)? = null
): Snackbar {
    return Snackbar.make(this, message, duration).also {
        buttonNameResId?.let { resId ->
            it.setAction(resId) { view -> buttonAction?.invoke(view) }
            actionTextColor?.let { ac -> it.setActionTextColor(ac) }
        }
        anchorView?.let { av -> it.setAnchorView(av) }
        it.show()
    }
}