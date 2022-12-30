package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.TransactionItem
import com.example.core.domain.model.Transaction

object DiffCallbackTransaction : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}