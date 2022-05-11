package com.example.bungkusyuk.helper

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

fun Context.getHelperColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getHelperDrawable(drawable: Int): Drawable {
    return ContextCompat.getDrawable(this, drawable)!!
}