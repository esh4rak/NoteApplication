package com.eshrak.noteapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class NoteResponse(
    val __v: Int,
    @PrimaryKey val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userid: String
)