package com.eshrak.noteapplication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eshrak.noteapplication.data.api.QuoteAPI
import com.eshrak.noteapplication.data.models.Result


const val STARTING_PAGE_INDEX = 1

class QuotePagingSource(private val quoteAPI: QuoteAPI) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        return try {

            val position = params.key ?: STARTING_PAGE_INDEX
            val response = quoteAPI.getQuotes(position)

            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (position == response.body()!!.totalPages) null else position + 1
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}