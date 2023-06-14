package com.multiplexer.cricapp.fragments.team

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.adapters.TeamsParentAdapter
import com.multiplexer.cricapp.databinding.FragmentTeamsBinding
import com.multiplexer.cricapp.repositories.CricketRepository
import com.multiplexer.cricapp.viewmodels.CricketViewModel

class TeamsFragment : Fragment() {
    private lateinit var viewModel: CricketViewModel
    private lateinit var cricRepo: CricketRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressDialog: ProgressDialog
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        cricRepo = CricketRepository(requireContext())
        recyclerView = binding.teamParentRV
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        recyclerView.setHasFixedSize(true)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setItemViewCacheSize(900)
        recyclerView.layoutManager = LinearLayoutManager(context)

        cricRepo.getAllCountries().observe(viewLifecycleOwner) {
            recyclerView.adapter = TeamsParentAdapter(
                requireContext(), viewModel, it
            )
            progressDialog.dismiss()
        }

    }
}