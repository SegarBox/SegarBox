package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    val id: Int,

    val name: String,

    val email: String,

    val phone: String

): Parcelable
