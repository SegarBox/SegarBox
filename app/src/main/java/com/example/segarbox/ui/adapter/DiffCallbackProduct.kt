package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.remote.response.ProductItem

object DiffCallbackProduct : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
        return oldItem == newItem
    }
}