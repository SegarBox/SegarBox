package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.databinding.ItemRowFeatureBinding

class DummyAdapter: ListAdapter<DummyModel, DummyAdapter.DummyViewHolder>(DummyDiffCallback) {
    inner class DummyViewHolder(var binding: ItemRowFeatureBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val binding = ItemRowFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DummyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        Glide.with(context)
            .load(item.image)
            .circleCrop()
            .into(holder.binding.ivItem)

        if (position == itemCount - 1) {
            holder.binding.space.isVisible = true
        }

    }
}