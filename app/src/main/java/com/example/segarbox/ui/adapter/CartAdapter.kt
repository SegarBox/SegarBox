package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.remote.response.UserCartItem
import com.example.segarbox.databinding.ItemRowCartBinding
import com.example.segarbox.helper.formatProductSize
import com.example.segarbox.helper.formatToRupiah

class CartAdapter : ListAdapter<UserCartItem, CartAdapter.CartViewHolder>(DiffCallbackCart) {
    inner class CartViewHolder(var binding: ItemRowCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemRowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            Glide.with(context)
                .load(item.product.image)
                .into(ivItem)

            tvItemName.text = item.product.label
            tvItemPrice.text = item.product.price.formatToRupiah()
            tvItemSize.text = item.product.size.formatProductSize(context)

            checkBox.isChecked = item.isChecked == 1
        }

    }

}
