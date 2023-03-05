package com.example.segarbox.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Login
import com.example.core.domain.usecase.LoginUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun login(email: String, password: String): LiveData<Event<Resource<Login>>> =
        loginUseCase.login(email, password).asLiveData().map {
            Event(it)
        }

    fun getTheme(): LiveData<Event<Boolean>> =
        loginUseCase.getTheme().asLiveData().map {
            Event(it)
        }

    fun saveToken(token: String) =
        loginUseCase.saveToken(token)

    fun saveUserId(userId: Int) =
        loginUseCase.saveUserId(userId)

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}