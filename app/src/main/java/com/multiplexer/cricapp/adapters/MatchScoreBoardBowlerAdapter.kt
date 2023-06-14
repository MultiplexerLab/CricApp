package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.models.fixtures.Bowling
import com.multiplexer.cricapp.models.fixtures.Lineup

private const val TAG = "MatchScoreBoardBowlerAd"

class MatchScoreBoardBowlerAdapter(
    lineUpLists: List<Lineup>?, battingLists: List<Bowling>?
) : RecyclerView.Adapter<MatchScoreBoardBowlerAdapter.ItemViewHolder>() {
    private var lineUpList = lineUpLists
    private var battingList = battingLists

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerRunOrOverScored: TextView = view.findViewById(R.id.playerRunOrOverScored)
        val playerBallOrMaidenFaced: TextView = view.findViewById(R.id.playerBallOrMaidenFaced)
        val player4sOrRuns: TextView = view.findViewById(R.id.player4sOrRuns)
        val player6sOrWickets: TextView = view.findViewById(R.id.player6sOrWickets)
        val playerSROrER: TextView = view.findViewById(R.id.playerSROrER)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_batting_bowling_scoreboard_item, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val player = battingList?.get(position)
        Log.d(TAG, "onBindViewHolder: lineup - ${lineUpList?.size}")
        holder.apply {
            playerName.text = player?.bowler?.fullname
            playerRunOrOverScored.text = player?.overs.toString()
            playerBallOrMaidenFaced.text = player?.medians.toString()
            player4sOrRuns.text = player?.runs.toString()
            player6sOrWickets.text = player?.wickets.toString()
            playerSROrER.text = player?.rate.toString()
        }
    }

    override fun getItemCount(): Int = battingList?.size ?: 0

}