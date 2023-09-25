package com.eshrak.noteapplication.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eshrak.noteapplication.R
import com.eshrak.noteapplication.data.models.UserRequest
import com.eshrak.noteapplication.databinding.FragmentRegisterBinding
import com.eshrak.noteapplication.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        binding.btnSignUp.setOnClickListener {
            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

            authViewModel.registerUser(UserRequest("test2@gmail.com", "12345", "test"))

        }


        binding.btnLogin.setOnClickListener {
            //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

            authViewModel.loginUser(UserRequest("test2@gmail.com", "12345", "test"))
        }

        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}