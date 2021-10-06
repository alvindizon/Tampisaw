package com.alvindizon.tampisaw.core.utils

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter


fun PagingDataAdapter<*, *>.setLoadStateListener(
    isNotLoading: ((Boolean) -> Unit)? = null,
    isLoading: ((Boolean) -> Unit)? = null,
    isLoadStateError: ((Boolean) -> Unit)? = null,
    errorListener: ((Throwable) -> Unit)? = null
) {
    addLoadStateListener { loadState ->
        isNotLoading?.invoke(loadState.source.refresh is LoadState.NotLoading)
        isLoading?.invoke(loadState.source.refresh is LoadState.Loading)
        isLoadStateError?.invoke(loadState.source.refresh is LoadState.Error)
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
        errorState?.let { errorListener?.invoke(it.error) }
    }
}
