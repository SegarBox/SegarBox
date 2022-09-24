package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.core.data.source.remote.response.UserCartItem

object DiffCallbackCart : DiffUtil.ItemCallback<UserCartItem>() {
    override fun areItemsTheSame(oldItem: UserCartItem, newItem: UserCartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserCartItem, newItem: UserCartItem): Boolean {
        return oldItem == newItem
    }
}