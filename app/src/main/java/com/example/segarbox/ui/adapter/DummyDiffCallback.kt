package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.model.DummyModel

object DummyDiffCallback : DiffUtil.ItemCallback<DummyModel>() {
    override fun areItemsTheSame(oldItem: DummyModel, newItem: DummyModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DummyModel, newItem: DummyModel): Boolean {
        return oldItem == newItem
    }
}