package com.example.core.ui.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.source.remote.response.AddressItem
import com.example.core.databinding.ItemRowAddressBinding
import com.example.core.domain.model.Address
import com.example.core.ui.DiffCallbackAddress

class AddressAdapter(private val onItemAddressClickCallback: OnItemAddressClickCallback) :
    ListAdapter<Address, AddressAdapter.AddressViewHolder>(DiffCallbackAddress) {

    inner class AddressViewHolder(var binding: ItemRowAddressBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding =
            ItemRowAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            tvAddress.text = item.street
            space.isVisible = position == itemCount - 1
            root.setOnClickListener {
                onItemAddressClickCallback.onAddressClicked(item)
            }
            ivDelete.setOnClickListener {
                onItemAddressClickCallback.onStashClicked(item)
            }
        }
    }

    interface OnItemAddressClickCallback {
        fun onAddressClicked(address: Address)
        fun onStashClicked(address: Address)
    }
}