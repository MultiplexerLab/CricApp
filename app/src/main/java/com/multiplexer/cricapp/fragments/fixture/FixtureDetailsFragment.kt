package com.multiplexer.cricapp.fragments.fixture

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.multiplexer.cricapp.adapters.viewPagerAdapter.TeamInfoTabAdapter
import com.multiplexer.cricapp.databinding.FragmentFixtureDetailsBinding
import com.multiplexer.cricapp.models.fixtures.Run
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "FixtureDetailsFragment"

class FixtureDetailsFragment : Fragment() {
    private val args: FixtureDetailsFragmentArgs by navArgs()
    private var _binding: FragmentFixtureDetailsBinding? = null
    private lateinit var viewModel: CricketViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFixtureDetailsBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.teamInfoTab
        val viewpage = binding.teamInfoViewPager
        val tabAdapter = TeamInfoTabAdapter(childFragmentManager, lifecycle, args.fixtures)
        viewpage.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewpage) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Info"
                }
                else -> {
                    tab.text = "ScoreBoard"
                }
            }
        }.attach()

        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        val item = args.fixtures
        val runs = item.runs
        var team1: MutableList<Run>
        var team2: MutableList<Run>

        Log.d(TAG, "onViewCreated: Id = ${item.id}")
        Log.d(TAG, "onViewCreated: Id = ${item.elected}")

        binding.apply {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM hh:mm", Locale.getDefault())
            val targetTime = item.starting_at
            val date = targetTime?.let { formatter.parse(it) }
            val formattedDateTime = date?.let { outputFormat.format(it) }
            val targetDate: Date = targetTime?.let { formatter.parse(it) } as Date
            dateAndTime.text = formattedDateTime
            val countdown =
                object : CountDownTimer(targetDate.time - System.currentTimeMillis(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val timeLeft = millisUntilFinished / 1000
                        val hours = timeLeft / 3600
                        val minutes = (timeLeft % 3600) / 60
                        val seconds = timeLeft % 60
                        countdown.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    }

                    override fun onFinish() {
                        countdown.text = ""
                    }
                }
            countdown.start()


            if (runs != null) {
                team1 = runs.filter {
                    it.team_id == item.localteam_id
                } as MutableList<Run>
                team2 = runs.filter {
                    it.team_id == item.visitorteam_id
                } as MutableList<Run>

                if (team1.isNotEmpty()) {
                    "${team1.lastOrNull()?.score.toString()}/${team1.lastOrNull()?.wickets.toString()} (${team1.lastOrNull()?.overs.toString()})".also {
                        team1Score.text = it
                    }
                } else {
                    team1Score.text = "N/A"
                }
                if (team2.isNotEmpty()) {
                    "${team2.lastOrNull()?.score.toString()}/${team2.lastOrNull()?.wickets.toString()} (${team2.lastOrNull()?.overs.toString()})".also {
                        team2Score.text = it
                    }
                } else {
                    team2Score.text = "N/A"
                }
            } else {
                team1Score.text = "N/A"
                team2Score.text = "N/A"
            }

            viewModel.getTeamName(item.localteam_id!!.toLong())
                .observe(team1Name.context as LifecycleOwner) {
                    team1Name.text = it
                }
            viewModel.getTeamName(item.visitorteam_id!!.toLong())
                .observe(team1Name.context as LifecycleOwner) {
                    team2Name.text = it
                }
            viewModel.getLeagueName(item.league_id!!.toLong())
                .observe(view.context as LifecycleOwner) {
                    leagueName.text = it
                }

            viewModel.getImageFromTeam(item.localteam_id!!.toLong())
                .observe(team1Logo.context as LifecycleOwner) {
                    Picasso.get().load(it).fit().into(team1Logo)
                }
            viewModel.getImageFromTeam(item.visitorteam_id!!.toLong())
                .observe(team2Logo.context as LifecycleOwner) {
                    Picasso.get().load(it).fit().into(team2Logo)
                }
            when (item.status) {
                "Finished" -> {
                    liveStatus.text = item.status
                }
                "NS" -> {
                    liveStatus.text = "Not Started"
                }
                "Aban." -> {
                    liveStatus.text = "Abandoned"
                }
                else -> {
                    liveStatus.text = item.status
                }
            }
            matchRound.text = item.round
        }
    }
}