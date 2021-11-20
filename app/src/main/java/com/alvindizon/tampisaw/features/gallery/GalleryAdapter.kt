package com.alvindizon.tampisaw.features.gallery

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.utils.listener
import com.alvindizon.tampisaw.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide

class UnsplashDiff : DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

class GalleryAdapter(
    val onImageLoadFailed: () -> Unit = {},
    val onImageResourceReady: () -> Unit = {},
    val listener: (UnsplashPhoto, ItemGalleryBinding) -> Unit
) : PagingDataAdapter<UnsplashPhoto, GalleryAdapter.ViewHolder>(UnsplashDiff()) {

    inner class ViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: UnsplashPhoto) {
            binding.photo = photo
            binding.executePendingBindings()

            binding.imageView.background = ColorDrawable(Color.parseColor(photo.color))

            Glide.with(binding.imageView)
                .load(photo.urls.regular)
                .listener(
                    onLoadFailed = { onImageLoadFailed.invoke() },
                    onResourceReady = { onImageResourceReady.invoke() }
                )
                .thumbnail(Glide.with(binding.imageView).load(photo.urls.thumb).centerCrop())
                .into(binding.imageView)
                .clearOnDetach()

            if (photo.height != null && photo.width != null) {
                binding.imageView.aspectRatio = photo.height.toDouble() / photo.width.toDouble()
            }

            Glide.with(binding.avatar)
                .load(photo.user.profileImageUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_user)
                .into(binding.avatar)
                .clearOnDetach()

            itemView.setOnClickListener {
                listener.invoke(photo, binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGalleryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
        }
    }
}

