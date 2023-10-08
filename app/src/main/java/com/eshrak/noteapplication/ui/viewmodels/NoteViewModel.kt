package com.eshrak.noteapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eshrak.noteapplication.data.models.NoteRequest
import com.eshrak.noteapplication.data.repository.NoteRepository
import com.eshrak.noteapplication.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository, private val application: Application
) : ViewModel() {


    val noteLiveData get() = noteRepository.noteLiveData
    val statusLiveData get() = noteRepository.statusLiveData


    fun getNotes() {
        viewModelScope.launch {
            val isInternetConnected =
                NetworkUtils.isInternetAvailable(application.applicationContext)

            if (isInternetConnected) {
                noteRepository.getNotesFromApi()
            } else {
                noteRepository.getNotesFromRoom()
            }
        }
    }


    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }


    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }


    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

}