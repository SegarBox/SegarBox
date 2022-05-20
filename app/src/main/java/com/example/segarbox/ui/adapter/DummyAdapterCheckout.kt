package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.databinding.ItemRowCheckoutBinding

class DummyAdapterCheckout: ListAdapter<DummyModelCart, DummyAdapterCheckout.DummyViewHolder>(DummyDiffCallbackCart) {
    inner class DummyViewHolder(var binding: ItemRowCheckoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            Glide.with(context)
                .load(item.image)
                .into(holder.binding.ivItem)

            tvItemName.text = item.itemName
            tvItemVariant.text = item.qty
            tvItemPrice.text = context.getString(R.string.grandTotal, item.price.toString())
        }
    }
}