package com.multiplexer.cricapp.fragments.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.multiplexer.cricapp.adapters.viewPagerAdapter.SeriesTabAdapter
import com.multiplexer.cricapp.databinding.FragmentSeriesBinding
import com.multiplexer.cricapp.viewmodels.CricketViewModel

class SeriesFragment : Fragment() {
    private lateinit var viewModel: CricketViewModel
    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.seriesTabLayout
        val viewpage = binding.seriesViewPager
        val tabAdapter = SeriesTabAdapter(childFragmentManager, lifecycle)
        viewpage.adapter = tabAdapter
        TabLayoutMediator(tabLayout, viewpage) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Teams"
                }
                else -> {
                    tab.text = "Competitions"
                }
            }
        }.attach()
    }
}