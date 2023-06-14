package com.multiplexer.cricapp.fragments.team

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.adapters.PlayerListAdapter
import com.multiplexer.cricapp.databinding.FragmentTeamInfoBinding
import com.multiplexer.cricapp.models.fixtures.Run
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "TeamInfoFragment"

class TeamInfoFragment : Fragment() {
    private val args: TeamInfoFragmentArgs by navArgs()
    private var _binding: FragmentTeamInfoBinding? = null
    private lateinit var viewModel: CricketViewModel
    private lateinit var team1Rv: RecyclerView
    private lateinit var team2Rv: RecyclerView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamInfoBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        team1Rv = view.findViewById(R.id.team1PalyerRecyclerView)
        team1Rv.setHasFixedSize(true)
        team1Rv.isDrawingCacheEnabled = true
        team1Rv.setItemViewCacheSize(15)
        team1Rv.layoutManager = LinearLayoutManager(context)

        team2Rv = view.findViewById(R.id.team2PalyerRecyclerView)
        team2Rv.setHasFixedSize(true)
        team2Rv.isDrawingCacheEnabled = true
        team2Rv.setItemViewCacheSize(15)
        team2Rv.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        val item = args.fixture
        val runs = item.runs
        var team1: MutableList<Run>
        var team2: MutableList<Run>

        Log.d(TAG, "onViewCreated: Id = ${item.id}")
        Log.d(TAG, "onViewCreated: Id = ${item.elected}")

        val squad1 = item.lineup?.filter {
            it.lineup?.team_id == item.localteam_id
        }

        val squad2 = item.lineup?.filter {
            it.lineup?.team_id == item.visitorteam_id
        }

        team1Rv.adapter = squad1?.let { PlayerListAdapter(it) }
        team2Rv.adapter = squad2?.let { PlayerListAdapter(it) }

        binding.apply {
            viewModel.getTeamName(item.localteam_id!!.toLong()).observe(viewLifecycleOwner) {
                team1NameRVTitle.text = it
            }
            viewModel.getTeamName(item.visitorteam_id!!.toLong()).observe(viewLifecycleOwner) {
                team2NameRVTitle.text = it
            }

            viewModel.getLeagueName(item.league_id!!.toLong())
                .observe(view.context as LifecycleOwner) {
                    matchSeries.text = it
                }

            matchName.text = item.round
            matchDateTime.text = item.starting_at?.let { getDate(it) }
            if (TextUtils.isEmpty(item.venue?.city) or TextUtils.isEmpty(item.venue?.name)) {
                matchVanue.text = "Not Available"
            } else {
                matchVanue.text = "${item.venue?.name},${item.venue?.city}"
            }
            item.venue?.capacity.let {
                if (it == null) {
                    venueCapacity.text = "N/A"
                } else {
                    venueCapacity.text = item.venue?.capacity.toString()
                }
            }
            item.toss_won_team_id?.let {
                viewModel.getTeamName(it.toLong()).observe(viewLifecycleOwner) {
                    matchToss.text = "$it won the toss and opted for ${item.elected}"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(date: String): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)
        val outputDateFormat = SimpleDateFormat("MMMM d, yyyy, hh:mm a", Locale.getDefault())
        return parsedDate?.let { outputDateFormat.format(it) }
    }
}