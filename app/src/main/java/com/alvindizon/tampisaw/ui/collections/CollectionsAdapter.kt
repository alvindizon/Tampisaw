package com.alvindizon.tampisaw.ui.collections

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.databinding.ItemCollectionsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

const val TRANSITION_MILLIS = 330

class UnsplashDiff: DiffUtil.ItemCallback<UnsplashCollection>() {
    override fun areItemsTheSame(oldItem: UnsplashCollection, newItem: UnsplashCollection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashCollection, newItem: UnsplashCollection): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

class CollectionAdapter(val listener: (UnsplashCollection) -> Unit)
    : PagingDataAdapter<UnsplashCollection, CollectionAdapter.ViewHolder>(UnsplashDiff()) {

    inner class ViewHolder(private val binding: ItemCollectionsBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(collection: UnsplashCollection, position: Int) {

            binding.imageView.background = ColorDrawable(Color.parseColor(collection.color))

            Glide.with(binding.imageView)
                .load(collection.coverPhotoRegularUrl)
                .thumbnail(Glide.with(binding.imageView).load(collection.coverPhotoThumbUrl).centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade(TRANSITION_MILLIS))
                .into(binding.imageView)
                .clearOnDetach()

            binding.username.text = collection.fullname
            binding.lockIcon.isVisible = collection.private?: false
            binding.handle.text = collection.username
            binding.collectionTitle.text = collection.title
            binding.photoCount.text = collection.totalPhotos.toString() +  " photos"

            if(collection.coverPhotoHeight != null && collection.coverPhotoWidth != null) {
                binding.imageView.aspectRatio = collection.coverPhotoHeight.toDouble() / collection.coverPhotoWidth.toDouble()
            }

            Glide.with(binding.avatar)
                .load(collection.profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.avatar)
                .clearOnDetach()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCollectionsBinding.inflate(layoutInflater, parent, false)
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

