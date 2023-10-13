package com.eshrak.noteapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eshrak.noteapplication.data.models.QuoteRemoteKeys

@Dao
interface RemoteKeyDao {

    @Query("SELECT * FROM quoteremotekey WHERE id=:id")
    fun getRemoteKeys(id: String): QuoteRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllRemoteKeys(remoteKeys: List<QuoteRemoteKeys>)

    @Query("DELETE FROM quoteremotekey")
    fun deleteAllRemoteKeys()

}