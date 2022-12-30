package com.example.segarbox.ui.splash

import androidx.lifecycle.*
import com.example.core.domain.usecase.SplashUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val splashUseCase: SplashUseCase): ViewModel() {
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

    fun getTheme(): LiveData<Event<Boolean>> =
        splashUseCase.getTheme().asLiveData().map {
            Event(it)
        }

    fun getIntro(): LiveData<Event<Boolean>> =
        splashUseCase.getIntro().asLiveData().map {
            Event(it)
        }

}