package com.multiplexer.cricapp.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.multiplexer.cricapp.databinding.FragmentBowlingCareerBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel

class BowlingCareerFragment : Fragment() {
    private val args: BowlingCareerFragmentArgs by navArgs()
    private lateinit var _binding: FragmentBowlingCareerBinding
    private val binding get() = _binding
    private lateinit var viewModel: CricketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBowlingCareerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "ScoreBoard is Loading..."
        )
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        viewModel.getPlayerDetails(args.playerId)
        viewModel.cachedPlayerDetails.observe(viewLifecycleOwner) { player ->
            var counter = 0
            var matchesCount = 0
            var inningsCount = 0
            var runsCount = 0
            var ballsCount = 0
            var maidens = 0
            var wickets = 0
            var strikeRate = 0.0
            var econRate = 0.0
            var wides = 0

            binding.apply {
                val test = player.career?.filter {
                    it.type == "Test/5day"
                }

                val odi = player.career?.filter {
                    it.type == "ODI"
                }

                val t20 = player.career?.filter {
                    it.type == "T20"
                }

                test?.forEach {
                    counter++
                    matchesCount += it.bowling?.matches ?: 0
                    inningsCount += it.bowling?.innings ?: 0
                    runsCount += it.bowling?.runs ?: 0
                    ballsCount += it.bowling?.overs?.toInt()?.times(6) ?: 0
                    maidens += it.bowling?.medians ?: 0
                    wickets += it.bowling?.wickets ?: 0
                    strikeRate += it.bowling?.strike_rate?.div(counter) ?: 0.0
                    strikeRate = "%.2f".format(strikeRate).toDouble()
                    econRate += it.bowling?.econ_rate?.div(counter) ?: 0.0
                    econRate = "%.2f".format(econRate).toDouble()
                    wides += it.bowling?.wide ?: 0
                }
                testMatch.text = matchesCount.toString()
                testInnings.text = inningsCount.toString()
                testRuns.text = runsCount.toString()
                testBalls.text = ballsCount.toString()
                testMaidens.text = maidens.toString()
                testWickets.text = wickets.toString()
                testSR.text = strikeRate.toString()
                testEconRate.text = econRate.toString()
                testWides.text = wides.toString()

                odi?.forEach {
                    matchesCount = 0
                    inningsCount = 0
                    runsCount = 0
                    ballsCount = 0
                    maidens = 0
                    wickets = 0
                    counter = 0
                    econRate = 0.0
                    wides = 0
                    counter++
                    matchesCount += it.bowling?.matches ?: 0
                    inningsCount += it.bowling?.innings ?: 0
                    runsCount += it.bowling?.runs ?: 0
                    ballsCount += it.bowling?.overs?.toInt()?.times(6) ?: 0
                    maidens += it.bowling?.medians ?: 0
                    wickets += it.bowling?.wickets ?: 0
                    strikeRate += it.bowling?.strike_rate?.div(counter) ?: 0.0
                    strikeRate = "%.2f".format(strikeRate).toDouble()
                    econRate += it.bowling?.econ_rate?.div(counter) ?: 0.0
                    econRate = "%.2f".format(econRate).toDouble()
                    wides += it.bowling?.wide ?: 0
                }

                odiMatch.text = matchesCount.toString()
                odiInnings.text = inningsCount.toString()
                odiRuns.text = runsCount.toString()
                odiBalls.text = ballsCount.toString()
                odiMaidens.text = maidens.toString()
                odiWickets.text = wickets.toString()
                odiSR.text = strikeRate.toString()
                odiEconRate.text = econRate.toString()
                odiWides.text = wides.toString()

                t20?.forEach {
                    matchesCount = 0
                    inningsCount = 0
                    runsCount = 0
                    ballsCount = 0
                    maidens = 0
                    wickets = 0
                    counter = 0
                    econRate = 0.0
                    wides = 0
                    counter++
                    matchesCount += it.bowling?.matches ?: 0
                    inningsCount += it.bowling?.innings ?: 0
                    runsCount += it.bowling?.runs ?: 0
                    ballsCount += it.bowling?.overs?.toInt()?.times(6) ?: 0
                    maidens += it.bowling?.medians ?: 0
                    wickets += it.bowling?.wickets ?: 0
                    strikeRate += it.bowling?.strike_rate?.div(counter) ?: 0.0
                    strikeRate = "%.2f".format(strikeRate).toDouble()
                    econRate += it.bowling?.econ_rate?.div(counter) ?: 0.0
                    econRate = "%.2f".format(econRate).toDouble()
                    wides += it.bowling?.wide ?: 0
                }

                t20Match.text = matchesCount.toString()
                t20Innings.text = inningsCount.toString()
                t20Runs.text = runsCount.toString()
                t20Balls.text = ballsCount.toString()
                t20Maidens.text = maidens.toString()
                t20Wickets.text = wickets.toString()
                t20SR.text = strikeRate.toString()
                t20EconRate.text = econRate.toString()
                t20Wides.text = wides.toString()
            }
        }
        ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
    }
}