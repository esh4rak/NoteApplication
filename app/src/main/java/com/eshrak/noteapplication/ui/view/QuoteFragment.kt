package com.eshrak.noteapplication.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eshrak.noteapplication.databinding.FragmentQuoteBinding
import com.eshrak.noteapplication.paging.LoaderAdapter
import com.eshrak.noteapplication.paging.QuotePagingAdapter
import com.eshrak.noteapplication.ui.viewmodels.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalPagingApi
class QuoteFragment : Fragment() {

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!


    private val quoteViewModel by viewModels<QuoteViewModel>()
    private lateinit var adapter: QuotePagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuoteBinding.inflate(inflater, container, false)

        adapter = QuotePagingAdapter()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Recycler View
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(), footer = LoaderAdapter()
        )


        //Observers
        bindObservers()

        //Handler
        bindHandler()

    }


    private fun bindObservers() {

        quoteViewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.submitData(lifecycle, it)
        })
    }

    private fun bindHandler() {

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}