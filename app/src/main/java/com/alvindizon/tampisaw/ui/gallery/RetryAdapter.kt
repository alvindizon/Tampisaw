package com.alvindizon.tampisaw.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.databinding.ItemRetryBinding

// this adapter displays a progress indicator and a retry button. this is meant to be used as a footer/header
class RetryAdapter (private val retry: () -> Unit) : LoadStateAdapter<RetryAdapter.RetryViewHolder>() {
    override fun onBindViewHolder(holder: RetryViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RetryViewHolder {
        return RetryViewHolder.create(parent, retry)
    }

    class RetryViewHolder (
        private val binding: ItemRetryBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object{
            fun create(parent: ViewGroup, retry: () -> Unit): RetryViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_retry, parent, false)
                val binding = ItemRetryBinding.bind(view)
                return RetryViewHolder(binding, retry)
            }
        }

    }
}
