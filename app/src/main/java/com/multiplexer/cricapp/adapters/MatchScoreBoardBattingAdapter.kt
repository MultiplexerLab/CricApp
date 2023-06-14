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
import com.multiplexer.cricapp.models.fixtures.Batting
import com.multiplexer.cricapp.models.fixtures.Lineup
import kotlinx.coroutines.*
import java.util.*


private const val TAG = "MatchScoreBoardBattingA"

class MatchScoreBoardBattingAdapter(
    lineUpLists: List<Lineup>?, battingLists: List<Batting>?
) : RecyclerView.Adapter<MatchScoreBoardBattingAdapter.ItemViewHolder>() {
    private var lineUpList = lineUpLists
    private var battingList = battingLists

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerRunOrOverScored: TextView = view.findViewById(R.id.playerRunOrOverScored)
        val playerBallOrMaidenFaced: TextView = view.findViewById(R.id.playerBallOrMaidenFaced)
        val player4sOrRuns: TextView = view.findViewById(R.id.player4sOrRuns)
        val player6sOrWickets: TextView = view.findViewById(R.id.player6sOrWickets)
        val playerSROrER: TextView = view.findViewById(R.id.playerSROrER)
        val playerOutBy: TextView = view.findViewById(R.id.playerOutBy)
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

        Log.d(TAG, "onBindViewHolder: lineup - ${battingList?.size}")

        val outPlayerByBowler = lineUpList?.filter {
            player?.bowling_player_id == it.id
        }
        val outPlayerByCatch = lineUpList?.filter {
            player?.bowling_player_id == it.id
        }
        val outPlayerByRun = lineUpList?.filter {
            player?.bowling_player_id == it.id
        }
        val outPlayer = lineUpList?.filter {
            player?.bowling_player_id == it.id
        }

        holder.apply {
            playerName.text = player?.batsman?.fullname
            playerRunOrOverScored.text = player?.score.toString()
            playerBallOrMaidenFaced.text = player?.ball.toString()
            player4sOrRuns.text = player?.four_x.toString()
            player6sOrWickets.text = player?.six_x.toString()
            playerSROrER.text = player?.rate.toString()

            val uniqueNames = HashSet<String>()

            outPlayerByBowler?.forEach { it.fullname?.let { it1 -> uniqueNames.add(it1) } }
            outPlayerByCatch?.forEach { it.fullname?.let { it1 -> uniqueNames.add(it1) } }
            outPlayerByRun?.forEach { it.fullname?.let { it1 -> uniqueNames.add(it1) } }
            outPlayer?.forEach { it.fullname?.let { it1 -> uniqueNames.add(it1) } }

            playerOutBy.text = uniqueNames.joinToString(", ")
        }
    }

    override fun getItemCount(): Int = battingList?.size ?: 0

}
