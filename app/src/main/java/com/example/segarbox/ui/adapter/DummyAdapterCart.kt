package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.databinding.ItemRowCartBinding

class DummyAdapterCart: androidx.recyclerview.widget.ListAdapter<DummyModelCart, DummyAdapterCart.DummyViewHolder>(DummyDiffCallbackCart) {
    inner class DummyViewHolder(var binding: ItemRowCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        Glide.with(context)
            .load(item.image)
            .into(holder.binding.ivItem)

        holder.binding.apply {
            tvItemName.text = item.itemName
            tvItemVariant.text = item.qty
            tvItemPrice.text = context.getString(R.string.grandTotal, item.price.toString())
        }
    }
}