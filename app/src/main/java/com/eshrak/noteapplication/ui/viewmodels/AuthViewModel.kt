package com.eshrak.noteapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.data.models.UserResponse
import com.eshrak.noteapplication.data.repository.UserRepository
import com.eshrak.noteapplication.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch { userRepository.registerUser(userRequest) }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch { userRepository.loginUser(userRequest) }
    }


}