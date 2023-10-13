package com.eshrak.noteapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quoteremotekey")
data class QuoteRemoteKeys(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)