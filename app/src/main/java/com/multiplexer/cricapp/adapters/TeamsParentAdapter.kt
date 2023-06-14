package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.repositories.CricketRepository
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import java.util.*

@Suppress("DEPRECATION")
class TeamsParentAdapter(
    private val context: Context,
    private val viewModel: CricketViewModel,
    val list: List<com.multiplexer.cricapp.models.countries.Data>
) : RecyclerView.Adapter<TeamsParentAdapter.ItemViewHolder>() {
    private var tasksList = list
    private val repo = CricketRepository(context)

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val childRecyclerView: RecyclerView = view.findViewById(R.id.teamChildRV)
        val countryName: TextView = view.findViewById(R.id.countryName)
        val countryNameLayout: RelativeLayout = view.findViewById(R.id.countryNameLayout)
        val cardView: CardView = view.findViewById(R.id.cardId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_prent_list_item, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val country = tasksList[position]

        country.id?.let {
            repo.getAllTeamByCountryId(it).observe(context as LifecycleOwner) { team ->
                if (team.isNotEmpty() && team != null) {
                    holder.childRecyclerView.adapter = TeamsChildAdapter(
                        context, viewModel, team
                    )
                } else {
                    if (team.isNullOrEmpty()) {
                        tasksList = tasksList.toMutableList().apply { removeAt(position) }
                        notifyItemRemoved(position)
                    }
                }
            }
        }
        holder.countryName.text = country.name
        holder.childRecyclerView.visibility = View.GONE
        holder.countryNameLayout.setOnClickListener {
            if (holder.childRecyclerView.visibility == View.VISIBLE) {
                holder.childRecyclerView.visibility = View.GONE
            } else {
                holder.childRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount() = tasksList.size
}