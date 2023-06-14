package com.multiplexer.cricapp.fragments.team

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
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.adapters.FixtureByTeamAdapter
import com.multiplexer.cricapp.databinding.FragmentTeamDetailsWithFixturesBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.squareup.picasso.Picasso

class TeamDetailsWithFixturesFragment : Fragment() {
    private val args: TeamDetailsWithFixturesFragmentArgs by navArgs()
    private var _binding: FragmentTeamDetailsWithFixturesBinding? = null
    private lateinit var viewModel: CricketViewModel
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailsWithFixturesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "Loading Matches..."
        )
        recyclerView = binding.fixtureByLeague
        recyclerView.setHasFixedSize(true)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setItemViewCacheSize(900)
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.apply {
            args.team.apply {
                Picasso.get().load(args.team.image_path).fit().into(teamImage)
                teamName.text = name
                country_id?.let { countryId ->
                    viewModel.getCountryName(countryId).observe(viewLifecycleOwner) {
                        teamCountry.text = it
                    }
                }
                if (national_team == true) {
                    nationalTeamORNot.text = getString(R.string.national_team)
                } else {
                    nationalTeamORNot.text = getString(R.string.local_team)
                }
            }
        }

        args.team.id?.let { viewModel.getFixturesWithTeam(it) }
        viewModel.cachedFixturesWithTeam.observe(viewLifecycleOwner) {
            Sneaker.with(requireActivity()).setTitle("Success!!")
                .setMessage("Loaded successfully!!!").sneakSuccess()
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
            recyclerView.adapter = FixtureByTeamAdapter(
                requireContext(), viewModel, it
            )
        }

    }
}