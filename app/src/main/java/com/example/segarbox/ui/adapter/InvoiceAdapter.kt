package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.remote.response.ProductTransactionsItem
import com.example.segarbox.databinding.ItemRowCheckoutBinding
import com.example.segarbox.helper.formatProductSize
import com.example.segarbox.helper.formatToRupiah

class InvoiceAdapter(private val onItemInvoiceClickCallback: OnItemInvoiceClickCallback) :
    ListAdapter<ProductTransactionsItem, InvoiceAdapter.InvoiceViewHolder>(
        DiffCallbackProductTransactions) {
    inner class InvoiceViewHolder(var binding: ItemRowCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding =
            ItemRowCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            Glide.with(context)
                .load(item.image)
                .into(ivItem)

            tvItemName.text = item.label
            tvItemCount.text = item.productQty.toString()
            tvItemSize.text = item.size.formatProductSize(context)
            tvItemPrice.text = item.price.formatToRupiah()
            divider.isVisible = position != itemCount - 1

            root.setOnClickListener {
                onItemInvoiceClickCallback.onItemClicked(item.productId)
            }

        }

    }

    interface OnItemInvoiceClickCallback {
        fun onItemClicked(productId: Int)
    }
}