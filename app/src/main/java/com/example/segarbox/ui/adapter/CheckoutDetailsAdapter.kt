package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.core.data.source.remote.response.UserCartItem
import com.example.segarbox.databinding.ItemRowCheckoutBinding
import com.example.segarbox.core.utils.formatProductSize
import com.example.segarbox.core.utils.formatToRupiah

class CheckoutDetailsAdapter : ListAdapter<UserCartItem, CheckoutDetailsAdapter.CheckoutDetailsViewHolder>(DiffCallbackCart) {
    inner class CheckoutDetailsViewHolder(var binding: ItemRowCheckoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutDetailsViewHolder {
        val binding = ItemRowCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutDetailsViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            Glide.with(context)
                .load(item.product.image)
                .into(ivItem)
            tvItemName.text = item.product.label
            tvItemSize.text = item.product.size.formatProductSize(context)
            tvItemPrice.text = item.product.price.formatToRupiah()
            tvItemCount.text = item.productQty.toString()
            divider.isVisible = position != itemCount - 1
        }
    }
}