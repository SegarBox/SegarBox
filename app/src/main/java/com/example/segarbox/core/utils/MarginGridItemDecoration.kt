package com.example.segarbox.core.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginGridItemDecoration(private val margin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {

        with(outRect) {

            bottom = margin

            // First Row
            if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
                top = margin
            }

            // First Column
            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                left = margin
                right = margin / 2
            }
            // Second Column
            else {
                right = margin
                left = margin / 2
            }

        }
    }
}