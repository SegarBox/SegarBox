package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.data.local.model.ShippingModel

object DiffCallbackShipping : DiffUtil.ItemCallback<ShippingModel>() {
    override fun areItemsTheSame(oldItem: ShippingModel, newItem: ShippingModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ShippingModel, newItem: ShippingModel): Boolean {
        return oldItem == newItem
    }
}