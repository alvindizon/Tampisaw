package com.alvindizon.tampisaw.ui.gallery

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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

            binding.imageView.background = ColorDrawable(Color.parseColor(photo.color))

            Glide.with(binding.imageView)
                .load(photo.urls.regular)
                .thumbnail(Glide.with(binding.imageView).load(photo.urls.thumb).centerCrop())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
                .clearOnDetach()

            binding.username.text = photo.user.name
            binding.handle.isVisible = !photo.sponsored
            binding.labelSponsored.isVisible = photo.sponsored
            binding.handle.text = photo.user.username

            Glide.with(binding.avatar)
                .load(photo.user.profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.avatar)
                .clearOnDetach()
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

