package com.example.core.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.databinding.ItemRowMainBinding
import com.example.core.domain.model.Product
import com.example.core.utils.Code
import com.example.core.utils.formatProductSize
import com.example.core.utils.formatToRupiah
import com.example.core.utils.getColorFromAttr
import com.google.android.material.R.attr.colorSecondaryVariant

class StartShoppingAdapter(private val onItemStartShoppingClickCallback: OnItemStartShoppingClickCallback) :
    ListAdapter<Product, StartShoppingAdapter.StartShoppingViewHolder>(
        DiffCallbackProduct) {
    inner class StartShoppingViewHolder(var binding: ItemRowMainBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartShoppingViewHolder {
        val binding = ItemRowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StartShoppingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StartShoppingViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)


        val cardBackgroundColor = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused),
            ),

            intArrayOf(
                context.getColorFromAttr(colorSecondaryVariant),
                context.getColorFromAttr(colorSecondaryVariant)
            )
        )

        Glide.with(context)
            .load(item.image)
            .into(holder.binding.imageView)

        holder.binding.apply {
            tvName.text = item.label
            tvSize.text = item.size.formatProductSize(context)
            tvPrice.text = item.price.formatToRupiah()
        }


        if (position == itemCount - 1 && (item.category == Code.DUMMY_VEGGIES || item.category == Code.DUMMY_FRUITS)) {
            holder.binding.apply {
                imageView.isVisible = false
                tvName.isVisible = false
                tvSize.isVisible = false
                tvPrice.isVisible = false

                tvSeeAll.isVisible = true

                root.elevation = 0F
                root.backgroundTintList = cardBackgroundColor

                root.setOnClickListener {
                    onItemStartShoppingClickCallback.onStartShoppingSeeAllClicked(item)
                }

            }
        } else {
            holder.binding.apply {
                imageView.isVisible = true
                tvName.isVisible = true
                tvSize.isVisible = true
                tvPrice.isVisible = true

                tvSeeAll.isVisible = false
                root.elevation = 12F

                val defaultColor = root.cardBackgroundColor
                root.backgroundTintList = defaultColor

                root.setOnClickListener {
                    onItemStartShoppingClickCallback.onItemStartShoppingClicked(item.id)
                }
            }
        }
    }

    interface OnItemStartShoppingClickCallback {
        fun onItemStartShoppingClicked(productId: Int)
        fun onStartShoppingSeeAllClicked(item: Product)
    }
}