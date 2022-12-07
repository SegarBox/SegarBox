package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.TransactionItem

object DiffCallbackTransaction : DiffUtil.ItemCallback<TransactionItem>() {
    override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem == newItem
    }
}