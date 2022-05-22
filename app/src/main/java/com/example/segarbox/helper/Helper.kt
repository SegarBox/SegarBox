package com.example.segarbox.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng

fun Context.getHelperColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getHelperDrawable(drawable: Int): Drawable {
    return ContextCompat.getDrawable(this, drawable)!!
}

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrColor, typedValue, true)
    return typedValue.data
}

fun LatLng.formatted(): String {
    return "${this.latitude.toString()},${this.longitude.toString()}"
}