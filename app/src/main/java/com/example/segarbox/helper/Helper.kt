package com.example.segarbox.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.segarbox.R
import com.google.android.gms.maps.model.LatLng
import java.text.NumberFormat
import java.util.*

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

fun String.tidyUpTikiEtd(context: Context): String {
    return context.getString(R.string.etd, this)
}

fun String.tidyUpJneEtd(context: Context): String {
    val split = this.split("-")
    var etd = ""

    for (i in split.indices) {
        etd += if (i == 0) {
            split[i]
        } else {
            " -${split[i]}"
        }
    }
    return context.getString(R.string.etd, etd)
}

fun String.tidyUpPosEtd(context: Context): String {
    val split = this.split(" ")
    return context.getString(R.string.etd, split[0])
}

fun Int.formatToRupiah(): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
}