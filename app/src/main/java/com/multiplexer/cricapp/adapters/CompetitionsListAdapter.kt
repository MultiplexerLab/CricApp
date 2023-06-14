package com.multiplexer.cricapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.fragments.series.SeriesFragmentDirections
import com.squareup.picasso.Picasso

private const val TAG = "CompetitionsListAdapter"

class CompetitionsListAdapter(
    val context: Context,
    list: List<com.multiplexer.cricapp.models.leagues.Data>
) : RecyclerView.Adapter<CompetitionsListAdapter.ItemViewHolder>() {
    private var leagueList = list

    private var lastPosition = -1

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val competitionName: TextView = view.findViewById(R.id.competitionName)
        val leagueImage: ImageView = view.findViewById(R.id.leaguePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.competitions_list_item, parent, false)
        return ItemViewHolder(root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val league = leagueList[position]
        holder.competitionName.text = league.name

        Picasso.get().load(league.image_path).fit().into(holder.leagueImage)
        holder.itemView.setOnClickListener {
            val action =
                SeriesFragmentDirections.actionSeriesFragmentToFixturesByLeagueFragment(league)

            Log.d(TAG, "onBindViewHolder: ${league.id}")
            Navigation.findNavController(holder.itemView).navigate(action)
        }
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return leagueList.size
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}
