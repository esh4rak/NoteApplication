package com.eshrak.noteapplication.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.eshrak.noteapplication.data.api.QuoteAPI
import com.eshrak.noteapplication.data.db.AppDatabase
import com.eshrak.noteapplication.paging.QuotePagingSource
import com.eshrak.noteapplication.paging.QuoteRemoteMediator
import javax.inject.Inject

@ExperimentalPagingApi
class QuoteRepository @Inject constructor(
    private val quoteAPI: QuoteAPI, private val appDatabase: AppDatabase
) {

    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { QuotePagingSource(quoteAPI) }
    ).liveData


    /*fun getQuotes() = Pager(config = PagingConfig(pageSize = 20, maxSize = 100),
        remoteMediator = QuoteRemoteMediator(quoteAPI, appDatabase),
        pagingSourceFactory = { appDatabase.quoteDao().getQuotes() }).liveData*/

}