package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemRowCheckoutBinding
import com.example.core.domain.model.TransactionProduct
import com.example.core.utils.formatProductSize
import com.example.core.utils.formatToRupiah

class InvoiceAdapter(private val onItemInvoiceClickCallback: OnItemInvoiceClickCallback) :
    ListAdapter<TransactionProduct, InvoiceAdapter.InvoiceViewHolder>(
        DiffCallbackTransactionProduct) {
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