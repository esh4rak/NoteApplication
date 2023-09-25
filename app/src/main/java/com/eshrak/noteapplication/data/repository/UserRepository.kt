package com.eshrak.noteapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eshrak.noteapplication.data.api.UserAPI
import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.data.models.UserResponse
import com.eshrak.noteapplication.util.Constants.TAG
import com.eshrak.noteapplication.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signUp(userRequest)
        handleResponse(response)
    }


    suspend fun loginUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signIn(userRequest)
        handleResponse(response)
    }


    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))

        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}