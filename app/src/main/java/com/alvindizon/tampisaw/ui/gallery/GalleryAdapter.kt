package com.alvindizon.tampisaw.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class UnsplashDiff: DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

class GalleryAdapter(val listener: (UnsplashPhoto) -> Unit)
    : PagingDataAdapter<UnsplashPhoto, GalleryAdapter.ViewHolder>(UnsplashDiff()) {

    inner class ViewHolder(private val binding: ItemGalleryBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: UnsplashPhoto, position: Int) {

            Glide.with(binding.imageView)
                .load(photo.urls.regular)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.imageView)

            binding.textViewUserName.text = photo.user.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGalleryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, position)
            holder.itemView.setOnClickListener {
                listener(item)
            }
        }
    }
}

