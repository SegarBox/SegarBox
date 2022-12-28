package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.remote.response.ProductTransactionsItem
import com.example.core.domain.model.TransactionProduct

object DiffCallbackTransactionProduct : DiffUtil.ItemCallback<TransactionProduct>() {
    override fun areItemsTheSame(oldItem: TransactionProduct, newItem: TransactionProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionProduct, newItem: TransactionProduct): Boolean {
        return oldItem == newItem
    }
}