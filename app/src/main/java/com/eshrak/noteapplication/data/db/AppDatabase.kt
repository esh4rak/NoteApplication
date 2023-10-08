package com.eshrak.noteapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eshrak.noteapplication.data.models.NoteResponse


@Database(entities = [NoteResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}