package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.remote.response.TransactionItem

object DiffCallbackTransaction : DiffUtil.ItemCallback<TransactionItem>() {
    override fun areItemsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionItem, newItem: TransactionItem): Boolean {
        return oldItem == newItem
    }
}