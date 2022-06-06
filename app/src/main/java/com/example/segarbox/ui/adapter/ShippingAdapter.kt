package com.example.segarbox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.model.ShippingModel
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.databinding.ItemRowShippingBinding
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.tidyUpJneEtd
import com.example.segarbox.helper.tidyUpPosEtd
import com.example.segarbox.helper.tidyUpTikiEtd

class ShippingAdapter(private val onItemClickCallback: OnItemClickCallback): ListAdapter<ShippingModel, ShippingAdapter.ShippingViewHolder>(DiffCallbackShipping) {
    inner class ShippingViewHolder(var binding: ItemRowShippingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingViewHolder {
        val binding = ItemRowShippingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShippingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShippingViewHolder, position: Int) {
        val context = holder.binding.root.context
        val item = getItem(position)

        holder.binding.apply {
            when (item.code) {
                Code.TIKI.uppercase() -> {
                    Glide.with(context)
                        .load(R.drawable.tiki)
                        .into(ivKurir)

                    tvEtd.text = item.etd.tidyUpTikiEtd(context)
                }

                Code.JNE.uppercase() -> {
                    Glide.with(context)
                        .load(R.drawable.jne)
                        .into(ivKurir)

                    tvEtd.text = item.etd.tidyUpJneEtd(context)
                }
                else -> {
                    Glide.with(context)
                        .load(R.drawable.pos)
                        .into(ivKurir)

                    tvEtd.text = item.etd.tidyUpPosEtd(context)
                }
            }

            tvKurir.text = item.code
            tvService.text = item.service
            tvPrice.text = item.price.formatToRupiah()

            space.isVisible = position == itemCount - 1

            root.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(shippingModel: ShippingModel)
    }
}