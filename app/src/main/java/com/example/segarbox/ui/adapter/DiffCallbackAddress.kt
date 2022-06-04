package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.remote.response.AddressItem

object DiffCallbackAddress : DiffUtil.ItemCallback<AddressItem>() {
    override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem == newItem
    }
}