package com.example.segarbox.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel(): ViewModel() {
    private val _clickCount = MutableLiveData(0)
    val clickCount: LiveData<Int> = _clickCount

    private val _isDelay = MutableLiveData(true)
    val isDelay: LiveData<Boolean> = _isDelay

    fun incrementClickCount() {
        _clickCount.value?.let {
            _clickCount.postValue(it.plus(1))
        }
    }

    fun setIsDelay(isDelay: Boolean) {
        _isDelay.postValue(isDelay)
    }

}