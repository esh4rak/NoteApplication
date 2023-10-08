package com.eshrak.noteapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eshrak.noteapplication.data.models.NoteResponse

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: List<NoteResponse>)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NoteResponse>

}
