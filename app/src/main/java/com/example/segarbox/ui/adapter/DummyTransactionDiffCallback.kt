package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyModelTransaction

object DummyTransactionDiffCallback : DiffUtil.ItemCallback<DummyModelTransaction>() {
    override fun areItemsTheSame(oldItem: DummyModelTransaction, newItem: DummyModelTransaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DummyModelTransaction, newItem: DummyModelTransaction): Boolean {
        return oldItem == newItem
    }
}