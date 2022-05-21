package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.model.DummyModel

object DummyDiffCallbackAddress : DiffUtil.ItemCallback<DummyAddress>() {
    override fun areItemsTheSame(oldItem: DummyAddress, newItem: DummyAddress): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DummyAddress, newItem: DummyAddress): Boolean {
        return oldItem == newItem
    }
}