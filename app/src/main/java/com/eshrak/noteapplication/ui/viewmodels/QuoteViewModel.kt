package com.eshrak.noteapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.eshrak.noteapplication.data.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class QuoteViewModel @Inject constructor(private val quoteRepository: QuoteRepository) :
    ViewModel() {

    val list = quoteRepository.getQuotes().cachedIn(viewModelScope)

}