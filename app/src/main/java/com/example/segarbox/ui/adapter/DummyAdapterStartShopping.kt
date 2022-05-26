package com.example.segarbox.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.databinding.ItemRowMainBinding
import com.example.segarbox.helper.getColorFromAttr
import com.google.android.material.R.attr.colorSecondaryVariant

class DummyAdapterStartShopping: ListAdapter<DummyModel, DummyAdapterStartShopping.DummyViewHolder>(DummyDiffCallback) {
    inner class DummyViewHolder(var binding: ItemRowMainBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
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


        if (position == itemCount - 1) {
            holder.binding.apply {
                imageView.isVisible = false
                tvName.isVisible = false
                tvQty.isVisible = false
                tvPrice.isVisible = false

                tvSeeAll.isVisible = true

                root.elevation = 0F
                root.backgroundTintList = cardBackgroundColor

            }
        } else {
            holder.binding.apply {
                imageView.isVisible = true
                tvName.isVisible = true
                tvQty.isVisible = true
                tvPrice.isVisible = true

                tvSeeAll.isVisible = false
                root.elevation = 12F

                val defaultColor = root.cardBackgroundColor
                root.backgroundTintList = defaultColor

            }
        }
    }
}