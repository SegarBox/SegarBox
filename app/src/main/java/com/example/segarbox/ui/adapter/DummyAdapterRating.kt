package com.example.segarbox.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.databinding.ItemRowRatingBinding


class DummyAdapterRating: ListAdapter<DummyModel, DummyAdapterRating.DummyViewHolder>(DummyDiffCallback) {
    inner class DummyViewHolder(var binding: ItemRowRatingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        Glide.with(context)
            .load(item.image)
            .circleCrop()
            .into(holder.binding.ivItem)
        holder.binding.tvItemName.text = "Cauliflower"
        holder.binding.tvItemVariant.text = item.qty

        holder.binding.ratingBar.setOnRatingBarChangeListener { _, _, _ ->
            holder.binding.materialButton2.isVisible = true
            holder.binding.materialButton2.setOnClickListener{
                Log.e("aaaa", holder.binding.ratingBar.rating.toString())
            }
        }
    }
}