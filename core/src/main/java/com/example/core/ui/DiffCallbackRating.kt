package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.RatingItem
import com.example.core.domain.model.Rating

object DiffCallbackRating : DiffUtil.ItemCallback<Rating>() {
    override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem == newItem
    }
}