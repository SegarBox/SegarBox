package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.RatingItem

object DiffCallbackRating : DiffUtil.ItemCallback<RatingItem>() {
    override fun areItemsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem == newItem
    }
}