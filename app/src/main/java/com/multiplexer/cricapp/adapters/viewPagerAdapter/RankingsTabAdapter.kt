package com.multiplexer.cricapp.adapters.viewPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.multiplexer.cricapp.fragments.player.PlayersRankingFragment
import com.multiplexer.cricapp.fragments.team.TeamsRankingFragment

class RankingsTabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = PlayersRankingFragment()
                val args = Bundle()
                args.putInt("positionId", 1)
                fragment.arguments = args
                fragment
            }
            1 -> {
                val fragment = PlayersRankingFragment()
                val args = Bundle()
                args.putInt("positionId", 2)
                fragment.arguments = args
                fragment
            }
            2 -> {
                val fragment = PlayersRankingFragment()
                val args = Bundle()
                args.putInt("positionId", 4)
                fragment.arguments = args
                fragment
            }
            else -> {
                val fragment = TeamsRankingFragment()
                val args = Bundle()
                args.putString("type", "Teams")
                fragment.arguments = args
                fragment
            }
        }
    }
}