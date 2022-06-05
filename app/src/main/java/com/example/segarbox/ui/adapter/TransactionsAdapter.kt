package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.remote.response.TransactionItem
import com.example.segarbox.databinding.ItemRowHistoryBinding
import com.example.segarbox.helper.formatItemCount
import com.example.segarbox.helper.formatSimpleDate
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.formatTotalCountItem

class TransactionsAdapter(private val onItemTransactionsClickCallback: OnItemTransactionsClickCallback) :
    ListAdapter<TransactionItem, TransactionsAdapter.TransactionsViewHolder>(DiffCallbackTransaction) {
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