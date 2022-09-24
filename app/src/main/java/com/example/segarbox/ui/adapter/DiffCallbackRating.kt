package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.core.data.source.remote.response.RatingItem

object DiffCallbackRating : DiffUtil.ItemCallback<RatingItem>() {
    override fun areItemsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RatingItem, newItem: RatingItem): Boolean {
        return oldItem == newItem
    }
}