package com.multiplexer.cricapp.fragments.team

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.multiplexer.cricapp.adapters.MatchScoreBoardBattingAdapter
import com.multiplexer.cricapp.adapters.MatchScoreBoardBowlerAdapter
import com.multiplexer.cricapp.databinding.FragmentScoreBoardTeamInfoBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel

private const val TAG = "ScoreBoardTeamInfoFragm"

class ScoreBoardTeamInfoFragment : Fragment() {
    private val args: ScoreBoardTeamInfoFragmentArgs by navArgs()
    private var _binding: FragmentScoreBoardTeamInfoBinding? = null
    private lateinit var viewModel: CricketViewModel
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBoardTeamInfoBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fixture = args.fixture
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        binding.apply {
            binding.team1BattingScoreboardRV.setHasFixedSize(true)
            binding.team1BattingScoreboardRV.isDrawingCacheEnabled = true
            binding.team1BattingScoreboardRV.setItemViewCacheSize(900)
            binding.team1BattingScoreboardRV.layoutManager = LinearLayoutManager(requireContext())
            val team1BattingList = fixture.batting?.filter {
                fixture.localteam_id == it.team_id
            }
            val team1BowlingList = fixture.bowling?.filter {
                fixture.localteam_id == it.team_id
            }

            val team2BattingList = fixture.batting?.filter {
                fixture.visitorteam_id == it.team_id
            }
            val team2BowlingList = fixture.bowling?.filter {
                fixture.visitorteam_id == it.team_id
            }
            val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
                requireContext(), "ScoreBoard is Loading..."
            )
            viewModel.getTeamName(fixture.localteam_id!!.toLong())
                .observe(team1BattingRVTitle.context as LifecycleOwner) {
                    team1BattingRVTitle.text = "$it Scores"
                }

            viewModel.getTeamName(fixture.visitorteam_id!!.toLong())
                .observe(team1BattingRVTitle.context as LifecycleOwner) {
                    team2BattingRVTitle.text = "$it Scores"
                }

            Log.d(TAG, "onViewCreated: batting size- ${team1BattingList?.size}")

            team1BattingScoreboardRV.adapter =
                MatchScoreBoardBattingAdapter(fixture.lineup, team1BattingList)
            team1BowlingScoreboard.adapter =
                MatchScoreBoardBowlerAdapter(fixture.lineup, team1BowlingList)
            team1BattingScoreboardRV.visibility = View.GONE
            team1BowlingScoreboard.visibility = View.GONE

            team2BattingScoreboardRV.adapter =
                MatchScoreBoardBattingAdapter(fixture.lineup, team2BattingList)
            team2BowlingScoreboard.adapter =
                MatchScoreBoardBowlerAdapter(fixture.lineup, team2BowlingList)
            team2BattingScoreboardRV.visibility = View.GONE
            team2BowlingScoreboard.visibility = View.GONE

            batsmanHead.visibility = View.GONE
            bowlerHead.visibility = View.GONE
            batsman1Head.visibility = View.GONE
            bowler1Head.visibility = View.GONE

            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
            team1BattingRVTitle.setOnClickListener {
                if (team1BattingScoreboardRV.visibility == View.VISIBLE) {
                    team1BattingScoreboardRV.visibility = View.GONE
                    team1BowlingScoreboard.visibility = View.GONE
                    batsmanHead.visibility = View.GONE
                    bowlerHead.visibility = View.GONE
                } else {
                    team1BattingScoreboardRV.visibility = View.VISIBLE
                    team1BowlingScoreboard.visibility = View.VISIBLE
                    batsmanHead.visibility = View.VISIBLE
                    bowlerHead.visibility = View.VISIBLE
                }
            }

            team2BattingRVTitle.setOnClickListener {
                if (team2BattingScoreboardRV.visibility == View.VISIBLE) {
                    team2BattingScoreboardRV.visibility = View.GONE
                    team2BowlingScoreboard.visibility = View.GONE
                    batsman1Head.visibility = View.GONE
                    bowler1Head.visibility = View.GONE
                } else {
                    team2BattingScoreboardRV.visibility = View.VISIBLE
                    team2BowlingScoreboard.visibility = View.VISIBLE
                    batsman1Head.visibility = View.VISIBLE
                    bowler1Head.visibility = View.VISIBLE
                }
            }
        }
    }
}