package com.multiplexer.cricapp.fragments.fixture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irozon.sneaker.Sneaker
import com.multiplexer.cricapp.adapters.FixtureByLeagueAdapter
import com.multiplexer.cricapp.databinding.FragmentFixturesByLeagueBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.squareup.picasso.Picasso

class FixturesByLeagueFragment : Fragment() {
    private val args: FixturesByLeagueFragmentArgs by navArgs()
    private lateinit var viewModel: CricketViewModel
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentFixturesByLeagueBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFixturesByLeagueBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.actionBar?.apply {
            title = "My Fragment Title"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "Loading Matches..."
        )
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        recyclerView = binding.fixtureByLeague
        recyclerView.setHasFixedSize(true)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setItemViewCacheSize(900)
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.apply {
            Picasso.get().load(args.league.image_path).fit().into(leagueImage)
            leagueName.text = args.league.name
        }

        args.league.id?.let { viewModel.getFixturesWithLeague(it) }
        viewModel.cachedFixturesWithLeague.observe(viewLifecycleOwner) {
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
            Sneaker.with(requireActivity()).setTitle("Success!!")
                .setMessage("Loaded successfully!!!").sneakSuccess()
            recyclerView.adapter = FixtureByLeagueAdapter(
                requireContext(), viewModel, it
            )
        }
    }
}