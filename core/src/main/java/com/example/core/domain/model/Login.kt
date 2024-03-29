package com.example.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(

    val token: String?,

    val user: User?,

    val loginError: LoginError?,

    val message: String?

): Parcelable

@Parcelize
data class LoginError(

    val password: List<String>?,

    val email: List<String>?

): Parcelable
