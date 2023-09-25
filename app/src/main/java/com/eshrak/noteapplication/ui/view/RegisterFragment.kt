package com.eshrak.noteapplication.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eshrak.noteapplication.R
import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.databinding.FragmentRegisterBinding
import com.eshrak.noteapplication.ui.viewmodels.AuthViewModel
import com.eshrak.noteapplication.util.NetworkResult
import com.eshrak.noteapplication.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignUp.setOnClickListener {

            val userRequest = getUserRequest()

            val validationResult = validateUserInput(userRequest)

            if (validationResult.first) {
                authViewModel.registerUser(userRequest)
            } else {
                binding.txtError.text = validationResult.second
            }

        }


        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObservers()

    }


    private fun getUserRequest(): UserRequest {

        val username = binding.txtUsername.text.toString()
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(emailAddress, password, username)
    }

    private fun validateUserInput(
        userRequest: UserRequest
    ): Pair<Boolean, String> {

        return authViewModel.validateCredentials(
            userRequest.username, userRequest.email, userRequest.password, false
        )
    }

    private fun bindObservers() {

        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {

                is NetworkResult.Success -> {

                    binding.progressbar.visibility = View.GONE

                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.progressbar.visibility = View.GONE
                    binding.txtError.text = it.message

                }

                is NetworkResult.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}