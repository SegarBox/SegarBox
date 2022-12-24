package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemRowCartBinding
import com.example.core.domain.model.Cart
import com.example.core.utils.formatProductSize
import com.example.core.utils.formatToRupiah

class CartAdapter(private val onItemCartClickCallback: OnItemCartClickCallback) :
    ListAdapter<Cart, CartAdapter.CartViewHolder>(DiffCallbackCart) {
    inner class CartViewHolder(var binding: ItemRowCartBinding) :
        RecyclerView.ViewHolder(binding.root)

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
            counter.tvCount.text = item.productQty.toString()
            checkBox.isChecked = item.isChecked == 1

            // Jika stok = 0
            if (item.product.qty == 0) {
                onItemCartClickCallback.onStashClicked(item)
            }

            // Jika permintaan > stok dan stok != 0, maka counter qty lgsg convert ke stok
            if ((item.productQty > item.product.qty) && item.product.qty != 0) {
                val newCart = item.copy(
                    productQty = item.product.qty
                )
                onItemCartClickCallback.onItemProductQtyChanged(newCart)
            }

            root.setOnClickListener {
                onItemCartClickCallback.onRootClicked(item)
            }

            ivStash.setOnClickListener {
                onItemCartClickCallback.onStashClicked(item)
            }

            checkBox.setOnClickListener {
                onItemCartClickCallback.onCheckboxClicked(item)
            }

            counter.ivRemove.setOnClickListener {
                if (item.productQty == 1) {
                    onItemCartClickCallback.onStashClicked(item)
                } else {
                    onItemCartClickCallback.onRemoveClicked(item)
                }
            }

            counter.ivAdd.setOnClickListener {
                onItemCartClickCallback.onAddClicked(item)
            }

        }

    }

    interface OnItemCartClickCallback {
        fun onCheckboxClicked(item: Cart)
        fun onRemoveClicked(item: Cart)
        fun onAddClicked(item: Cart)
        fun onStashClicked(item: Cart)
        fun onRootClicked(item: Cart)
        fun onItemProductQtyChanged(item: Cart)
    }

}
