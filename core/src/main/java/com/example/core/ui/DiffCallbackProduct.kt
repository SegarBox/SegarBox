package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.model.Product

object DiffCallbackProduct : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}