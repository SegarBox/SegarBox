package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.AddressModel

object DummyDiffCallbackAddress : DiffUtil.ItemCallback<AddressModel>() {
    override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem == newItem
    }
}