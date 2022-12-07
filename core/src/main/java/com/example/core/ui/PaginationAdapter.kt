package com.example.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.databinding.ItemRowMainBinding
import com.example.core.utils.formatProductSize
import com.example.core.utils.formatToRupiah
import com.example.core.utils.getCardResponsiveWidth

class PaginationAdapter(private val onItemPaginationClickCallback: OnItemPaginationClickCallback) :
    PagingDataAdapter<ProductItem, PaginationAdapter.PaginationViewHolder>(DiffCallbackProduct) {
    inner class PaginationViewHolder(var binding: ItemRowMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaginationViewHolder {
        val binding = ItemRowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaginationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaginationViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {

            val newLayoutParams = root.layoutParams
            newLayoutParams.width = getCardResponsiveWidth()
            root.layoutParams = newLayoutParams

            item?.let { item ->
                Glide.with(context)
                    .load(item.image)
                    .into(imageView)

                tvName.text = item.label
                tvPrice.text = item.price.formatToRupiah()
                tvSize.text = item.size.formatProductSize(context)

                root.setOnClickListener {
                    onItemPaginationClickCallback.onItemPaginationClicked(item.id)
                }
            }

        }
    }

    interface OnItemPaginationClickCallback {
        fun onItemPaginationClicked(productId: Int)
    }

}