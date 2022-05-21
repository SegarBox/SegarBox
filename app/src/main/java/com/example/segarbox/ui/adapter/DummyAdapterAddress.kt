package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.databinding.ItemRowAddressBinding

class DummyAdapterAddress: ListAdapter<DummyAddress, DummyAdapterAddress.DummyViewHolder>(DummyDiffCallbackAddress) {
    inner class DummyViewHolder(var binding: ItemRowAddressBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            tvAddress.text = item.address
            space.isVisible = position == itemCount - 1
        }
    }

}