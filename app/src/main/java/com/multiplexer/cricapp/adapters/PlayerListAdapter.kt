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
import com.multiplexer.cricapp.fragments.fixture.FixtureDetailsFragmentDirections
import com.multiplexer.cricapp.models.fixtures.Lineup
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.util.*

@Suppress("DEPRECATION")
class PlayerListAdapter(
    list: List<Lineup>
) : RecyclerView.Adapter<PlayerListAdapter.ItemViewHolder>() {
    private var playerList = list

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerPosition: TextView = view.findViewById(R.id.playerPosition)
        val playerImage: ImageView = view.findViewById(R.id.playerPicture)
        val playerCard: CardView = view.findViewById(R.id.playerCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.player_list_item, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val player = playerList[position]
        holder.playerName.text = player.fullname
        when (player.position?.name) {
            "Wicketkeeper" -> holder.playerPosition.text = "(W)"
            else -> holder.playerPosition.text = ""
        }

        Picasso.get().load(player.image_path).fit().into(holder.playerImage)


        holder.playerCard.setOnClickListener {
            val action =
                FixtureDetailsFragmentDirections.actionFixtureDetailsFragmentToPlayerDetailsFragment(
                    player
                )
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }
}
