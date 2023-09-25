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
import com.eshrak.noteapplication.databinding.FragmentLoginBinding
import com.eshrak.noteapplication.ui.viewmodels.AuthViewModel
import com.eshrak.noteapplication.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener {

            val userRequest = getUserRequest()

            val validationResult = validateUserInput(userRequest)

            if (validationResult.first) {
                authViewModel.loginUser(userRequest)
            } else {
                binding.txtError.text = validationResult.second
            }

        }


        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        bindObservers()
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {

                is NetworkResult.Success -> {

                    //Token
                    binding.progressbar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
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


    private fun getUserRequest(): UserRequest {

        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress, password, "")
    }

    private fun validateUserInput(
        userRequest: UserRequest
    ): Pair<Boolean, String> {

        return authViewModel.validateCredentials("", userRequest.email, userRequest.password, true)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}