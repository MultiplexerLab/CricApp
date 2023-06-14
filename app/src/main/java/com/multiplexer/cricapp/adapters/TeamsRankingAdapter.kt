package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
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
import com.multiplexer.cricapp.fragments.ranking.RankingsFragmentDirections
import com.multiplexer.cricapp.models.teamRanking.Data
import com.multiplexer.cricjass.R
import com.squareup.picasso.Picasso
import java.util.*

@Suppress("DEPRECATION")
class TeamsRankingAdapter(
    val list: Data
) : RecyclerView.Adapter<TeamsRankingAdapter.ItemViewHolder>() {
    private var tasksList = list.team

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamName: TextView = view.findViewById(R.id.teamName)
        val teamPicture: ImageView = view.findViewById(R.id.teamPicture)
        val teamPosition: TextView = view.findViewById(R.id.teamPosition)
        val teamCard: CardView = view.findViewById(R.id.teamCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.team_item_list, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val team = tasksList?.get(position)

        team?.apply {
            holder.apply {
                teamName.text = name
                Picasso.get().load(image_path).fit().into(teamPicture)
                teamPosition.text = team.position.toString()
            }
        }

        holder.teamCard.setOnClickListener {
            val action = team?.let { it1 ->
                RankingsFragmentDirections.actionRankingsFragmentToTeamDetailsWithFixturesFragment(
                    it1
                )
            }
            if (action != null) {
                Navigation.findNavController(holder.itemView).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = tasksList?.size ?: 0
}
