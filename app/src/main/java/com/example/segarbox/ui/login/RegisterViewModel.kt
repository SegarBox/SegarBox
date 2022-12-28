package com.example.segarbox.ui.login

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.RegisterResponse
import com.example.core.domain.model.Register
import com.example.core.domain.usecase.RegisterUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): LiveData<Event<Resource<Register>>> =
        registerUseCase.register(name, email, phone, password, password_confirmation).asLiveData().map {
            Event(it)
        }

    fun saveToken(token: String) =
        registerUseCase.saveToken(token)

    fun saveUserId(userId: Int) =
        registerUseCase.saveUserId(userId)

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _registerResponse = MutableLiveData<RegisterResponse>()
//    val registerResponse: LiveData<RegisterResponse> = _registerResponse
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun register(
//        name: String,
//        email: String,
//        phone: String,
//        password: String,
//        password_confirmation: String
//    ) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val response =
//                retrofitRepository.register(name, email, phone, password, password_confirmation)
//            _registerResponse.postValue(response)
//            _isLoading.postValue(false)
//        }
//    }
}