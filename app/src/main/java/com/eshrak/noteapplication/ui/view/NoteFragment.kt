package com.eshrak.noteapplication.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.eshrak.noteapplication.data.models.NoteRequest
import com.eshrak.noteapplication.data.models.NoteResponse
import com.eshrak.noteapplication.databinding.FragmentNoteBinding
import com.eshrak.noteapplication.ui.viewmodels.NoteViewModel
import com.eshrak.noteapplication.util.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : Fragment() {


    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()


    private var note: NoteResponse? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandler()
        bindObserver()
    }


    private fun setInitialData() {

        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {

            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.noteTitleET.setText(it.title)
                binding.noteDescriptionET.setText(it.description)
            }

        } else {
            binding.title.text = "Add Note"
        }
    }


    private fun bindHandler() {

        binding.deleteButton.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }

        binding.submitButton.setOnClickListener {

            val title = binding.noteTitleET.text.toString().trim()
            val description = binding.noteDescriptionET.text.toString().trim()
            val noteResult = NoteRequest(description, title)
            if (note == null) {
                noteViewModel.createNote(noteResult)
            } else {
                noteViewModel.updateNote(note!!._id, noteResult)
            }
        }
    }


    private fun bindObserver() {

        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}