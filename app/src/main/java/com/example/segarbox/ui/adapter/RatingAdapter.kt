package com.example.segarbox.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.remote.response.AddressItem
import com.example.segarbox.data.remote.response.RatingItem
import com.example.segarbox.databinding.ItemRowAddressBinding
import com.example.segarbox.databinding.ItemRowRatingBinding
import com.example.segarbox.helper.formatSimpleDate

class RatingAdapter(private val onItemRatingClickCallback: OnItemRatingClickCallback): ListAdapter<RatingItem, RatingAdapter.RatingViewHolder>(DiffCallbackRating) {
    inner class RatingViewHolder(var binding: ItemRowRatingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = ItemRowRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            Glide.with(context)
                .load(item.image)
                .into(ivItem)
            tvItemName.text = item.label
            tvDate.text = item.createdAt.formatSimpleDate()
            space.isVisible = position == itemCount - 1

            root.setOnClickListener{
                onItemRatingClickCallback.onRootClicked(item.transactionId)
            }

            ratingBar.setOnRatingBarChangeListener { _, rate, _ ->
                onItemRatingClickCallback.onRate(item.productId, rate.toDouble())
            }
        }

    }

    interface OnItemRatingClickCallback {
        fun onRate(productId: Int, rate: Double)
        fun onRootClicked(transactionId: Int)
    }

}