package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.fragments.series.SeriesFragmentDirections
import com.multiplexer.cricapp.models.teamRanking.Team
import com.multiplexer.cricapp.repositories.CricketRepository
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.squareup.picasso.Picasso
import java.util.*

@Suppress("DEPRECATION")
class TeamsChildAdapter(
    context: Context,
    private val viewModel: CricketViewModel,
    val list: List<com.multiplexer.cricapp.models.teams.Data>
) : RecyclerView.Adapter<TeamsChildAdapter.ItemViewHolder>() {
    private var tasksList = list
    private val repo = CricketRepository(context)

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamName: TextView = view.findViewById(R.id.teamName)
        val childCardView: CardView = view.findViewById(R.id.childCardView)
        val teamLogo: ImageView = view.findViewById(R.id.teamImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_child_list_item, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val team = tasksList[position]

        if (tasksList.isNullOrEmpty()) {
            holder.itemView.rootView.findViewById<TextView>(R.id.parentItem).visibility = View.GONE
        }

        if (team.name != null) {
            holder.teamName.text = team.name
            Picasso.get().load(team.image_path).fit().into(holder.teamLogo)
        }

        val currentTeam = Team(
            team.code,
            team.country_id,
            team.id,
            team.image_path,
            team.name,
            team.national_team,
            null,
            null,
            team.resource,
            team.updated_at
        )

        holder.childCardView.setOnClickListener {
            val action =
                SeriesFragmentDirections.actionSeriesFragmentToTeamDetailsWithFixturesFragment(
                    currentTeam
                )
            Navigation.findNavController(holder.itemView).navigate(action)
        }


    }

    override fun getItemCount(): Int {
        return tasksList.size
    }
}