package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.core.data.source.remote.response.ProductTransactionsItem

object DiffCallbackProductTransactions : DiffUtil.ItemCallback<ProductTransactionsItem>() {
    override fun areItemsTheSame(oldItem: ProductTransactionsItem, newItem: ProductTransactionsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductTransactionsItem, newItem: ProductTransactionsItem): Boolean {
        return oldItem == newItem
    }
}