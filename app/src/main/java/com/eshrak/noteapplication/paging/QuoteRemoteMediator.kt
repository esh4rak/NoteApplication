package com.eshrak.noteapplication.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.eshrak.noteapplication.data.api.QuoteAPI
import com.eshrak.noteapplication.data.db.AppDatabase
import com.eshrak.noteapplication.data.models.QuoteRemoteKeys
import com.eshrak.noteapplication.data.models.Result


@ExperimentalPagingApi
class QuoteRemoteMediator(
    private val quoteAPI: QuoteAPI,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Result>() {

    private val quoteDao = appDatabase.quoteDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {


        //Fetch Quotes From API
        //Save these quotes + Remote Key data into DB
        //Login for states - REFRESH, PREPEND, APPEND


        return try {

            val currentPage = when (loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }


            val response = quoteAPI.getQuotes(currentPage)
            val endOfPaginationReached = response.body()!!.totalPages == currentPage


            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            appDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    quoteDao.deleteQuotes()
                    remoteKeyDao.deleteAllRemoteKeys()
                }

                quoteDao.addQuotes(response.body()!!.results)

                val keys = response.body()!!.results.map { quote ->
                    QuoteRemoteKeys(
                        id = quote._id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                remoteKeyDao.addAllRemoteKeys(keys)
            }

            MediatorResult.Success(endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Result>
    ): QuoteRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?._id?.let { id ->
                remoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Result>
    ): QuoteRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote._id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Result>
    ): QuoteRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { quote ->
                remoteKeyDao.getRemoteKeys(id = quote._id)
            }
    }

}

