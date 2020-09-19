package com.alvindizon.tampisaw.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.databinding.ItemPhotoTagBinding

class TagAdapter(private val listener: (String) -> Unit): ListAdapter<String, TagAdapter.ViewHolder>(tagDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoTagBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, position)
            holder.itemView.setOnClickListener {
                listener.invoke(item)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemPhotoTagBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, position: Int) {
            binding.tagChip.text = title
        }
    }

    companion object {
        private val tagDiff = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String)= oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String)= areItemsTheSame(oldItem, newItem)
        }

    }

}