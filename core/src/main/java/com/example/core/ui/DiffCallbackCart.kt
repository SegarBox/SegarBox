package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.UserCartItem

object DiffCallbackCart : DiffUtil.ItemCallback<UserCartItem>() {
    override fun areItemsTheSame(oldItem: UserCartItem, newItem: UserCartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserCartItem, newItem: UserCartItem): Boolean {
        return oldItem == newItem
    }
}