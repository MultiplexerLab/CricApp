package com.multiplexer.cricapp.fragments.player

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irozon.sneaker.Sneaker
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.adapters.PlayerRankingListAdapter
import com.multiplexer.cricapp.databinding.FragmentPlayersRankingBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel

private const val TAG = "PlayersRankingFragment"

class PlayersRankingFragment : Fragment() {
    private val args: PlayersRankingFragmentArgs by navArgs()
    private lateinit var viewModel: CricketViewModel
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentPlayersRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        _binding = FragmentPlayersRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "Loading Players..."
        )
        recyclerView = binding.playersRecycleView
        recyclerView.setHasFixedSize(true)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setItemViewCacheSize(900)
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        viewModel.getPlayerList(args.positionId)
        Log.d(TAG, "onViewCreated: entered")
        viewModel.cachedPlayerList.observe(viewLifecycleOwner) {
            Sneaker.with(requireActivity()).setTitle("Success!!")
                .setMessage("Players Loaded successfully!!!").sneakSuccess()
            Log.d(TAG, "onViewCreated: ${it.size}")
            recyclerView.adapter = PlayerRankingListAdapter(requireContext(), it)
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
        }

        val handler = Handler()
        handler.postDelayed({
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.main, menu)
        val item = menu.findItem(R.id.actionSearch)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val adapter = recyclerView.adapter as PlayerRankingListAdapter
                    adapter.filter(newText)
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}