package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemRowHistoryBinding
import com.example.core.domain.model.Transaction
import com.example.core.utils.formatItemCount
import com.example.core.utils.formatSimpleDate
import com.example.core.utils.formatToRupiah
import com.example.core.utils.formatTotalCountItem

class TransactionsAdapter(private val onItemTransactionsClickCallback: OnItemTransactionsClickCallback) :
    ListAdapter<Transaction, TransactionsAdapter.TransactionsViewHolder>(DiffCallbackTransaction) {
    inner class TransactionsViewHolder(var binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val binding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        if (item.productTransactions.isNotEmpty()) {
            holder.binding.apply {
                Glide.with(context)
                    .load(item.productTransactions[0].image)
                    .into(ivItem)
                if (item.qtyTransaction == 1) {
                    tvItemCountTotal.isVisible = false
                } else {
                    tvItemCountTotal.isVisible = true
                    tvItemCountTotal.text = (item.qtyTransaction - 1).formatTotalCountItem(context)
                }
                tvItemName.text = item.productTransactions[0].label
                tvDate.text = item.createdAt.formatSimpleDate()
                tvItemCount.text = item.productTransactions[0].productQty.formatItemCount(context)
                tvPriceCountTotal.text = item.totalPrice.formatToRupiah()
                btnCheck.isVisible = item.status == "inprogress"

                btnCheck.setOnClickListener {
                    onItemTransactionsClickCallback.onBtnClicked(item.id)
                }

                root.setOnClickListener {
                    onItemTransactionsClickCallback.onRootClicked(item.id)
                }
            }
        }

    }

    interface OnItemTransactionsClickCallback {
        fun onBtnClicked(transactionId: Int)
        fun onRootClicked(transactionId: Int)
    }
}