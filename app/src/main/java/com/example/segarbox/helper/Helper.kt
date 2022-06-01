package com.example.segarbox.helper

import android.R.attr.state_focused
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.segarbox.R
import com.example.segarbox.data.remote.response.ProductItem
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSecondaryVariant
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

fun Int.formatProductSize(context: Context): String {
    return context.getString(R.string.gram, this.toString())
}

fun addDummyProduct(dummyCategoryName: String, listSize: Int): ProductItem {
    return ProductItem("dummy", 1, 1, 1, listSize, "dummy", "dummy", dummyCategoryName)
}

fun String.tokenFormat(): String {
    return "Bearer $this"
}

fun getScreenWidthInPixel(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun Int.toPixel(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun getCardResponsiveWidth(): Int {
    return Math.floor((getScreenWidthInPixel() * 0.425)).toInt()
}

fun Context.getColorStateListSecondaryVariant(): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(-state_focused),
            intArrayOf(state_focused),
        ),

        intArrayOf(
            this.getColorFromAttr(colorSecondaryVariant),
            this.getColorFromAttr(colorSecondaryVariant)
        )
    )
}

fun Context.getColorStateListPrimary(): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(-state_focused),
            intArrayOf(state_focused),
        ),

        intArrayOf(
            this.getColorFromAttr(colorPrimary),
            this.getColorFromAttr(colorPrimary)
        )
    )
}