package com.example.segarbox.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModelTransaction
import com.example.segarbox.databinding.ItemRowHistoryBinding

class DummyAdapterTransaction(fragment: String): ListAdapter<DummyModelTransaction, DummyAdapterTransaction.DummyViewHolder>(DummyTransactionDiffCallback) {
    inner class DummyViewHolder (var binding: ItemRowHistoryBinding): RecyclerView.ViewHolder(binding.root)
    val fragment = fragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        Glide.with(context)
            .load(item.firstItemImage)
            .into(holder.binding.ivItem)

        holder.binding.apply {
            tvItemName.text = item.firstItemName
            tvItemCount.text = context.getString(R.string.firstItemCount, item.firstItemCount.toString())
            tvItemCountTotal.text = context.getString(R.string.totalMoreProductCount, item.totalMoreProductCount.toString())
            tvPriceCountTotal.text = context.getString(R.string.grandTotal, item.grandTotal.toString())
            btnCheck.isVisible = fragment == "In Progress"
        }
    }
}