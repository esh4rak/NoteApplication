package com.eshrak.noteapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eshrak.noteapplication.databinding.FragmentQuoteBinding


class QuoteFragment : Fragment() {

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Observers
        //bindObservers()

        //Handler
        bindHandler()

        //Get Quotes
        //noteViewModel.getNotes()
    }


    private fun bindHandler() {

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}