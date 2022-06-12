package com.example.segarbox.helper

import android.R.attr.state_focused
import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

private const val DEFAULT_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val DATE_TIME_PATTERN = "dd MMM yyyy, HH:mm"
private const val DATE_SIMPLE_PATTERN = "dd MMM yyyy"

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
    return "${this.latitude},${this.longitude}"
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
    val string = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this)
    val formatted = string.split(",")
    return formatted[0]
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
    return floor((getScreenWidthInPixel() * 0.425)).toInt()
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

fun Context.getColorStateListRed(): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(-state_focused),
            intArrayOf(state_focused),
        ),

        intArrayOf(
            this.getHelperColor(R.color.lightModeRed),
            this.getHelperColor(R.color.lightModeRed)
        )
    )
}

fun Int.formatTotalCountItem(context: Context): String {
    return context.resources.getQuantityString(R.plurals.item_count_total, this, this)
}

fun Int.formatItemCount(context: Context): String {
    return context.resources.getQuantityString(R.plurals.item_count, this, this)
}

@SuppressLint("SimpleDateFormat")
fun String.formatDateTime(): String {
    val defaultParser = SimpleDateFormat(DEFAULT_TIME_PATTERN)
    val formatter = SimpleDateFormat(DATE_TIME_PATTERN)
    defaultParser.timeZone = TimeZone.getTimeZone("GMT")

    val time = defaultParser.parse(this)
    return "${formatter.format(time!!)} WIB"
}

@SuppressLint("SimpleDateFormat")
fun String.formatSimpleDate(): String {
    val defaultParser = SimpleDateFormat(DEFAULT_TIME_PATTERN)
    val formatter = SimpleDateFormat(DATE_SIMPLE_PATTERN)
    defaultParser.timeZone = TimeZone.getTimeZone("GMT")

    val time = defaultParser.parse(this)
    return formatter.format(time!!)
}

fun String.formatStatus(): String {
    return if (this == "inprogress") {
        "In Progress"
    }
    else {
        "Completed"
    }
}