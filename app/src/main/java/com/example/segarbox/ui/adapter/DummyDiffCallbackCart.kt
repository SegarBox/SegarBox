package com.example.segarbox.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.segarbox.data.local.model.DummyModelCart

object DummyDiffCallbackCart : DiffUtil.ItemCallback<DummyModelCart>(){
    override fun areItemsTheSame(oldItem: DummyModelCart, newItem: DummyModelCart): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DummyModelCart, newItem: DummyModelCart): Boolean {
        return oldItem == newItem
    }
}