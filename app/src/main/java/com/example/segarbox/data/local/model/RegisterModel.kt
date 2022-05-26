package com.example.segarbox.data.local.model

data class RegisterModel(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val password_confirmation: String,
)