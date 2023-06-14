package com.multiplexer.cricapp.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.multiplexer.cricapp.databinding.FragmentBattingCareerBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel

class BattingCareerFragment : Fragment() {
    private val args: BattingCareerFragmentArgs by navArgs()
    private lateinit var _binding: FragmentBattingCareerBinding
    private val binding get() = _binding
    private lateinit var viewModel: CricketViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBattingCareerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        viewModel.getPlayerDetails(args.playerId)
        val progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "ScoreBoard is Loading..."
        )
        viewModel.cachedPlayerDetails.observe(viewLifecycleOwner) { player ->
            var matchesCount = 0
            var inningsCount = 0
            var runsCount = 0
            var ballsCount = 0
            var foursCount = 0
            var sixesCount = 0
            var average = 0.0
            var strikeRate = 0.0
            var highestScore = 0

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

                test?.forEach { career ->
                    matchesCount += career.batting?.matches ?: 0
                    inningsCount += career.batting?.innings ?: 0
                    runsCount += career.batting?.runs_scored ?: 0
                    ballsCount += career.batting?.balls_faced ?: 0
                    foursCount += career.batting?.four_x ?: 0
                    sixesCount += career.batting?.six_x ?: 0

                    career.batting?.average?.let {
                        if (it > average) {
                            average = it
                        }
                    }

                    career.batting?.highest_inning_score?.let {
                        if (it > highestScore) {
                            highestScore = it
                        }
                    }

                    career.batting?.strike_rate?.let { rate ->
                        if (rate.compareTo(strikeRate) > 0) {
                            strikeRate = career.batting.strike_rate
                        }
                    }
                }

                testMatch.text = matchesCount.toString()
                testInnings.text = inningsCount.toString()
                testRuns.text = runsCount.toString()
                testBalls.text = ballsCount.toString()
                testFours.text = foursCount.toString()
                testSixes.text = sixesCount.toString()
                testAverage.text = average.toString()
                testSR.text = strikeRate.toString()
                testHighest.text = highestScore.toString()


                odi?.forEach { career ->
                    matchesCount = 0
                    inningsCount = 0
                    runsCount = 0
                    ballsCount = 0
                    foursCount = 0
                    sixesCount = 0
                    average = 0.0
                    strikeRate = 0.0
                    highestScore = 0
                    matchesCount += career.batting?.matches ?: 0
                    inningsCount += career.batting?.innings ?: 0
                    runsCount += career.batting?.runs_scored ?: 0
                    ballsCount += career.batting?.balls_faced ?: 0
                    foursCount += career.batting?.four_x ?: 0
                    sixesCount += career.batting?.six_x ?: 0

                    career.batting?.average?.let {
                        if (it > average) {
                            average = it
                        }
                    }
                    career.batting?.highest_inning_score?.let {
                        if (it > highestScore) {
                            highestScore = it
                        }
                    }

                    career.batting?.strike_rate?.let { rate ->
                        if (rate.compareTo(strikeRate) > 0) {
                            strikeRate = career.batting.strike_rate
                        }
                    }
                }

                odiMatch.text = matchesCount.toString()
                odiInnings.text = inningsCount.toString()
                odiRuns.text = runsCount.toString()
                odiBalls.text = ballsCount.toString()
                odiFours.text = foursCount.toString()
                odiSixes.text = sixesCount.toString()
                odiAverage.text = average.toString()
                odiSR.text = strikeRate.toString()
                odiHighest.text = highestScore.toString()

                t20?.forEach { career ->
                    matchesCount = 0
                    inningsCount = 0
                    runsCount = 0
                    ballsCount = 0
                    foursCount = 0
                    sixesCount = 0
                    average = 0.0
                    strikeRate = 0.0
                    highestScore = 0
                    matchesCount += career.batting?.matches ?: 0
                    inningsCount += career.batting?.innings ?: 0
                    runsCount += career.batting?.runs_scored ?: 0
                    ballsCount += career.batting?.balls_faced ?: 0
                    foursCount += career.batting?.four_x ?: 0
                    sixesCount += career.batting?.six_x ?: 0

                    career.batting?.average?.let {
                        if (it > average) {
                            average = it
                        }
                    }

                    career.batting?.highest_inning_score?.let {
                        if (it > highestScore) {
                            highestScore = it
                        }
                    }

                    career.batting?.strike_rate?.let { rate ->
                        if (rate.compareTo(strikeRate) > 0) {
                            strikeRate = career.batting.strike_rate
                        }
                    }
                }

                t20Match.text = matchesCount.toString()
                t20Innings.text = inningsCount.toString()
                t20Runs.text = runsCount.toString()
                t20Balls.text = ballsCount.toString()
                t20Fours.text = foursCount.toString()
                t20Sixes.text = sixesCount.toString()
                t20Average.text = average.toString()
                t20SR.text = strikeRate.toString()
                t20Highest.text = highestScore.toString()
            }
        }
        ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
    }
}