package com.eshrak.noteapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eshrak.noteapplication.data.models.NoteResponse
import com.eshrak.noteapplication.data.models.QuoteRemoteKeys
import com.eshrak.noteapplication.data.models.Result


@Database(entities = [NoteResponse::class, Result::class, QuoteRemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun quoteDao(): QuoteDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}