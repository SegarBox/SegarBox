package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.data.remote.response.AddressData
import com.example.segarbox.databinding.ItemRowAddressBinding

class AddressAdapter(private val onItemAddressClickCallback: OnItemAddressClickCallback): ListAdapter<AddressData, AddressAdapter.AddressViewHolder>(DiffCallbackAddress) {
    inner class AddressViewHolder(var binding: ItemRowAddressBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemRowAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            tvAddress.text = item.street
            root.setOnClickListener {
                onItemAddressClickCallback.onAddressClicked(item)
            }
            ivDelete.setOnClickListener {
                onItemAddressClickCallback.onStashClicked(item)
            }
        }
    }

    interface OnItemAddressClickCallback {
        fun onAddressClicked(addressData: AddressData)
        fun onStashClicked(addressData: AddressData)
    }
}