package com.alvindizon.tampisaw.core.utils

import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.ui.gallery.LoadStateListener


@BindingAdapter("loadStateListener")
fun RecyclerView.bindLoadStateListener(listener: LoadStateListener) {
    (adapter as PagingDataAdapter<*,*>).apply {
        addLoadStateListener(listener)
    }

}