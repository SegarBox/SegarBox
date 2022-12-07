package com.example.core.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.R
import com.example.core.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit):  LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    inner class LoadingStateViewHolder(var binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        val context = holder.binding.root.context
        holder.binding.apply {
            btnRetry.setOnClickListener { retry.invoke() }

            if (loadState is LoadState.Error) {
                tvError.text = context.getString(R.string.rto)
            }
            btnRetry.isVisible = loadState is LoadState.Error
            tvError.isVisible = loadState is LoadState.Error
        }
    }

}