package com.multiplexer.cricapp.adapters.viewPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.multiplexer.cricapp.fragments.team.ScoreBoardTeamInfoFragment
import com.multiplexer.cricapp.fragments.team.TeamInfoFragment
import com.multiplexer.cricapp.models.fixtures.Data

class TeamInfoTabAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle, val fixture: Data
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = TeamInfoFragment()
                val args = Bundle()
                args.putParcelable("fixture", fixture)
                fragment.arguments = args
                fragment
            }
            else -> {
                val fragment = ScoreBoardTeamInfoFragment()
                val args = Bundle()
                args.putParcelable("fixture", fixture)
                fragment.arguments = args
                fragment
            }
        }
    }
}