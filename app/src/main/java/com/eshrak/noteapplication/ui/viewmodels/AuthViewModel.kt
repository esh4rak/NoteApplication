package com.eshrak.noteapplication.ui.viewmodels

import android.text.TextUtils
import android.util.Patterns
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


    fun validateCredentials(
        username: String, emailAddress: String, password: String, isLogin: Boolean
    ): Pair<Boolean, String> {


        var result = Pair(true, "")

        if ((!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email")
        } else if (password.length <= 4) {
            result = Pair(false, "Password length should be greater than 4")
        }

        return result

    }


}