package com.example.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Register(

    val token: String?,

    val user: User?,

    val registerError: RegisterError?,

    val message: String?

): Parcelable

@Parcelize
data class RegisterError(

    val password: List<String>?,

    val phone: List<String>?,

    val name: List<String>?,

    val email: List<String>?

): Parcelable
