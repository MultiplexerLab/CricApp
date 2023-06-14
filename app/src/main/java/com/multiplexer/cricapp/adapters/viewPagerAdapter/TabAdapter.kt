package com.multiplexer.cricapp.adapters.viewPagerAdapter

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.multiplexer.cricapp.fragments.fixture.CricketFragment
import java.util.*

private const val TAG = "TabAdapter"

class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = CricketFragment()
        val args = Bundle()
        when (position) {
            0 -> {
                args.putString("date", getDate(-3))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            1 -> {
                args.putString("date", getDate(-2))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            2 -> {
                args.putString("date", getDate(-1))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            3 -> {
                args.putString("date", getDate(0))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            4 -> {
                args.putString("date", getDate(1))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            5 -> {
                args.putString("date", getDate(2))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
            else -> {
                args.putString("date", getDate(3))
                fragment.arguments = args
                Log.d(TAG, "createFragment: ")
                return fragment
            }
        }
    }

    private fun getDate(diff: Int): String {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
        calendar.add(Calendar.DATE, diff)
        val nextDayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return if (diff == 0) {
            "${currentDate}T00:00:00.000000Z,${currentDate}T23:59:00.000000Z"
        } else {
            "${nextDayDate}T00:00:00.000000Z,${nextDayDate}T23:59:00.000000Z"
        }
    }
}