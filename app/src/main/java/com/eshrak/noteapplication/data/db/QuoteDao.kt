package com.eshrak.noteapplication.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eshrak.noteapplication.data.models.Result

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    fun getQuotes(): PagingSource<Int, Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuotes(quotes: List<Result>)

    @Query("DELETE FROM quotes")
    fun deleteQuotes()

}