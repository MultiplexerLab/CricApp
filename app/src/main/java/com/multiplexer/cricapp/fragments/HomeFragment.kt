package com.multiplexer.cricapp.fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.multiplexer.cricapp.adapters.viewPagerAdapter.TabAdapter
import com.multiplexer.cricapp.databinding.FragmentHomeBinding
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import java.util.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    private lateinit var viewModel: CricketViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout
        val viewpage = binding.viewPager2
        val tabAdapter = TabAdapter(childFragmentManager, lifecycle)
        continents()
        countries()
        teams()
        leagues()
        viewpage.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewpage) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getDate(-3)
                }
                1 -> {
                    tab.text = getDate(-2)
                }
                2 -> {
                    tab.text = getDate(-1)
                }
                3 -> {
                    tab.text = getDate(0)
                }
                4 -> {
                    tab.text = getDate(1)
                }
                5 -> {
                    tab.text = getDate(2)
                }
                else -> {
                    tab.text = getDate(3)
                }
            }
        }.attach()

        tabLayout.getTabAt(3)?.select()
    }

    private fun leagues() {
        if (viewModel.getAllLeagues().value == null) {
            viewModel.leagues.observe(context as LifecycleOwner) {
                viewModel.addAllLeague(it)
            }
        }
    }

    private fun getDate(diff: Int): String {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
        calendar.add(Calendar.DATE, diff)
        val dateFormat = SimpleDateFormat("EE", Locale.getDefault())
        val dayOfWeek = dateFormat.format(calendar.time)
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        return if (diff == 0) {
            "Today"
        } else {
            "$dayOfWeek $date"
        }
    }


    private fun teams() {
        if (viewModel.getAllTeams().value == null) {
            viewModel.teams.observe(context as LifecycleOwner) {
                viewModel.addAllTeams(it)
            }
        }
    }

    private fun continents() {
        if (viewModel.getAllContinents().value == null) {
            viewModel.continents.observe(context as LifecycleOwner) {
                viewModel.addAllContinents(it)
            }
        }
    }

    private fun countries() {
        if (viewModel.getAllCountries().value == null) {
            viewModel.countries.observe(context as LifecycleOwner) {
                viewModel.addAllCountry(it)
            }
        }
    }
}