package com.alvindizon.tampisaw.collections.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.collections.R
import com.alvindizon.tampisaw.collections.databinding.ItemCollectionsBinding
import com.alvindizon.tampisaw.collections.model.Collection
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

const val TRANSITION_MILLIS = 330

class UnsplashDiff : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(
        oldItem: Collection,
        newItem: Collection
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Collection,
        newItem: Collection
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

class CollectionAdapter(val listener: (Collection, ItemCollectionsBinding) -> Unit) :
    PagingDataAdapter<Collection, CollectionAdapter.ViewHolder>(UnsplashDiff()) {

    inner class ViewHolder(private val binding: ItemCollectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(collection: Collection) {
            binding.collection = collection
            binding.executePendingBindings()

            binding.imageView.background = ColorDrawable(Color.parseColor(collection.color))

            Glide.with(binding.imageView)
                .load(collection.coverPhotoRegularUrl)
                .thumbnail(
                    Glide.with(binding.imageView).load(collection.coverPhotoThumbUrl).centerCrop()
                )
                .transition(DrawableTransitionOptions.withCrossFade(TRANSITION_MILLIS))
                .into(binding.imageView)
                .clearOnDetach()

            if (collection.coverPhotoHeight != null && collection.coverPhotoWidth != null) {
                binding.imageView.aspectRatio =
                    collection.coverPhotoHeight.toDouble() / collection.coverPhotoWidth.toDouble()
            }

            Glide.with(binding.avatar)
                .load(collection.profileImageUrl)
                .placeholder(R.drawable.ic_user)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.avatar)
                .clearOnDetach()

            itemView.setOnClickListener { listener(collection, binding) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCollectionsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.bind(item) }
    }
}

