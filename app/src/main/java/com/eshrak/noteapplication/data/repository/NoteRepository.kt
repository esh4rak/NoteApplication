package com.eshrak.noteapplication.data.repository

import com.eshrak.noteapplication.data.db.NoteDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eshrak.noteapplication.data.api.NoteApi
import com.eshrak.noteapplication.data.models.NoteRequest
import com.eshrak.noteapplication.data.models.NoteResponse
import com.eshrak.noteapplication.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteApi: NoteApi, private val noteDao: NoteDao
) {


    private val _noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _noteLiveData


    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: MutableLiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun getNotesFromApi() {
        try {

            val response = noteApi.getNotes()

            if (response.isSuccessful && response.body() != null) {

                // Cache the data in the Room database
                val notes = response.body()!!
                withContext(Dispatchers.IO) {
                    noteDao.insertNotes(notes)
                }

                _noteLiveData.postValue(NetworkResult.Success(notes))

            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _noteLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _noteLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        } catch (e: Exception) {
            // Handle exceptions, such as network errors
            _noteLiveData.postValue(NetworkResult.Error("An error occurred: ${e.message}"))
        }
    }


    suspend fun getNotesFromRoom() {
        try {
            // Fetch data from the Room database on a background thread
            val localNotes = withContext(Dispatchers.IO) {
                noteDao.getAllNotes()
            }

            if (localNotes.isNotEmpty()) {
                _noteLiveData.postValue(NetworkResult.Success(localNotes))
            } else {
                _noteLiveData.postValue(NetworkResult.Error("No Data Available"))
            }
        } catch (e: Exception) {
            // Handle exceptions, such as database errors
            _noteLiveData.postValue(NetworkResult.Error("An error occurred: ${e.message}"))
        }
    }


    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleResponse(response, "Note Created")

    }


    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }


    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }


    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))

        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}