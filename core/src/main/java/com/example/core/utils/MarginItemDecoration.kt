package com.example.core.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val margin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {

        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = margin
            }
            right = margin
            top = margin
            bottom = margin + (margin / 2)

        }
    }
}