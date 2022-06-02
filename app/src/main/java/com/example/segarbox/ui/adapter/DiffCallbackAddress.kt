package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.data.local.model.ShippingModel
import com.example.segarbox.data.remote.response.AddressData

object DiffCallbackAddress : DiffUtil.ItemCallback<AddressData>() {
    override fun areItemsTheSame(oldItem: AddressData, newItem: AddressData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddressData, newItem: AddressData): Boolean {
        return oldItem == newItem
    }
}