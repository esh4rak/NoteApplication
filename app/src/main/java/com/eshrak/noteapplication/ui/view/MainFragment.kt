package com.eshrak.noteapplication.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eshrak.noteapplication.R
import com.eshrak.noteapplication.data.models.NoteResponse
import com.eshrak.noteapplication.databinding.FragmentMainBinding
import com.eshrak.noteapplication.ui.adapters.NoteAdapter
import com.eshrak.noteapplication.ui.viewmodels.NoteViewModel
import com.eshrak.noteapplication.util.Constants.TAG
import com.eshrak.noteapplication.util.NetworkResult
import com.eshrak.noteapplication.util.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NoteAdapter

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setNavigationDrawer()

        adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Recycler View
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter

        //Observers
        bindObservers()

        //Handler
        bindHandler()

        //Get Notes
        noteViewModel.getNotes()


        // Create an OnBackPressedCallback to handle back press
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )


    }

    private fun bindHandler() {

        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        // Set up navigation item clicks
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    binding.drawerLayout.closeDrawers()
                }

                R.id.nav_settings -> {
                    binding.drawerLayout.closeDrawers()
                }

                R.id.nav_share -> {
                    binding.drawerLayout.closeDrawers()
                }

                R.id.nav_about -> {
                    binding.drawerLayout.closeDrawers()
                }

                R.id.nav_quote -> {
                    binding.drawerLayout.closeDrawers()
                    findNavController().navigate(R.id.action_mainFragment_to_quoteFragment)
                }

                R.id.nav_logout -> {
                    binding.drawerLayout.closeDrawers()
                    tokenManager.clearSharedPreferences()
                    findNavController().navigate(R.id.action_mainFragment_to_registerFragment)
                }
            }

            true
        }

    }


    private fun bindObservers() {

        noteViewModel.noteLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)

                    if (it.data!!.isEmpty()) {
                        binding.nothingLayout.visibility = View.VISIBLE
                    } else {
                        binding.nothingLayout.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.nothingLayout.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun setNavigationDrawer() {

        val headerView = binding.navigationView.getHeaderView(0)
        val title = headerView.findViewById<TextView>(R.id.nav_drawer_title)
        val subtitle = headerView.findViewById<TextView>(R.id.nav_drawer_subtitle)

        if (tokenManager.getUserName() != null) {
            title.text = tokenManager.getUserName()
        } else {
            Log.d(TAG, "User Name Not Found")
        }

        if (tokenManager.getUserEmail() != null) {
            subtitle.text = tokenManager.getUserEmail()
        } else {
            Log.d(TAG, "Email Not Found")
        }

    }

    private fun onNoteClicked(noteResponse: NoteResponse) {

        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}