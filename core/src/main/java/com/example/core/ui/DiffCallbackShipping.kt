package com.example.core.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.model.ShippingModel

object DiffCallbackShipping : DiffUtil.ItemCallback<ShippingModel>() {
    override fun areItemsTheSame(oldItem: ShippingModel, newItem: ShippingModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ShippingModel, newItem: ShippingModel): Boolean {
        return oldItem == newItem
    }
}