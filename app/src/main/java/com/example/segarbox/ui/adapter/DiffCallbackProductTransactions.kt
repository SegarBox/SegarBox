package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.remote.response.AddressItem
import com.example.segarbox.data.remote.response.ProductTransactionsItem
import com.example.segarbox.data.remote.response.TransactionItem

object DiffCallbackProductTransactions : DiffUtil.ItemCallback<ProductTransactionsItem>() {
    override fun areItemsTheSame(oldItem: ProductTransactionsItem, newItem: ProductTransactionsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductTransactionsItem, newItem: ProductTransactionsItem): Boolean {
        return oldItem == newItem
    }
}