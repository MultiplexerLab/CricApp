package com.multiplexer.cricapp.fragments.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.multiplexer.cricapp.databinding.FragmentPlayerInfoMainBinding
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerInfoMainFragment : Fragment() {
    private val args: PlayerInfoMainFragmentArgs by navArgs()
    private var _binding: FragmentPlayerInfoMainBinding? = null
    private lateinit var viewModel: CricketViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerInfoMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        viewModel.getPlayerDetails(args.playerId)
        viewModel.cachedPlayerDetails.observe(viewLifecycleOwner) { player ->
            binding.apply {
                playerFullName.text = player.fullname
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
                val inputDate = player.dateofbirth?.let { inputDateFormat.parse(it) }
                val outputDateStr = inputDate?.let { outputDateFormat.format(it) }
                dateOfBirth.text = outputDateStr
                playerRole.text = player.position?.name
                battingStyle.text = player.battingstyle?.uppercase(Locale.getDefault())
                bowlingStyle.text = player.bowlingstyle?.uppercase(Locale.getDefault())
            }
        }
    }
}