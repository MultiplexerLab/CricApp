package com.multiplexer.cricapp.fragments.player

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.adapters.viewPagerAdapter.PlayerInfoTabAdapter
import com.multiplexer.cricapp.databinding.FragmentPlayerDetailsBinding
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PlayerDetailsFragment : Fragment() {
    private val args: PlayerDetailsFragmentArgs by navArgs()
    private var _binding: FragmentPlayerDetailsBinding? = null
    private lateinit var viewModel: CricketViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerDetailsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.playerInfoTab
        val viewpage = binding.teamInfoViewPager
        val tabAdapter = PlayerInfoTabAdapter(childFragmentManager, lifecycle, args.player)
        viewpage.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewpage) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Info"
                }
                1 -> {
                    tab.text = "Batting"
                }
                else -> {
                    tab.text = "Bowling"
                }
            }
        }.attach()

        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        val player = args.player

        binding.apply {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(0xFF532563.toInt(), 0xFF23104A.toInt())
            )

            cardId.background = gradientDrawable
            playerFirstName.text = player.firstname
            playerLastName.text = player.lastname
            when (player.position?.name) {
                "Batsman" -> positionLogo.setImageResource(R.drawable.cricket_bat)
                "Bowler" -> positionLogo.setImageResource(R.drawable.cricket_ball)
                "Wicketkeeper" -> positionLogo.setImageResource(R.drawable.cricket_stamp)
                else -> positionLogo.setImageResource(R.drawable.cricket_bat_ball)
            }
            Picasso.get().load(player.image_path).fit().into(playerImage)
            player.country_id?.let { countryId ->
                viewModel.getCountryName(countryId).observe(viewLifecycleOwner) {
                    countryAndAge.text = "$it . ${calculateAge(player.dateofbirth)}yrs"
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(birthDateString: String?): Int {
        val birthDate = LocalDate.parse(birthDateString)
        val currentDate = LocalDate.now()
        return ChronoUnit.YEARS.between(birthDate, currentDate).toInt()
    }
}