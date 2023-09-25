package com.eshrak.noteapplication.data.api

import com.eshrak.noteapplication.data.models.NoteRequest
import com.eshrak.noteapplication.data.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

    @GET("/note/getNotes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/note/createNote")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("/note/createNote/{noteId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String, @Body noteRequest: NoteRequest
    ): Response<NoteResponse>


    @DELETE("/note/deleteNote/{noteId}")
    suspend fun deleteNote(
        @Path("noteId") noteId: String
    ): Response<NoteResponse>
}