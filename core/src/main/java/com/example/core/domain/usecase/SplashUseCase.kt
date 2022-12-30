package com.example.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SplashUseCase {
    fun getTheme(): Flow<Boolean>

    fun getIntro(): Flow<Boolean>
}