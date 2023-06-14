package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.fragments.ranking.RankingsFragmentDirections
import com.multiplexer.cricapp.models.fixtures.Lineup
import com.multiplexer.cricapp.models.playersList.Data
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.util.*


private const val TAG = "PlayerRankingListAdapte"

@Suppress("DEPRECATION")
class PlayerRankingListAdapter(
    val context: Context,
    val list: List<Data>
) : RecyclerView.Adapter<PlayerRankingListAdapter.ItemViewHolder>() {
    private var playerList = list
    private var lastPosition = -1
    private var isScrollingUp = false

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerImage: ImageView = view.findViewById(R.id.playerPicture)
        val playerCard: CardView = view.findViewById(R.id.playerCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_ranking_list_item, parent, false)
        return ItemViewHolder(root)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val player = playerList[position]
        holder.playerName.text = player.fullname
        Picasso.get().load(player.image_path).fit().into(holder.playerImage)

        val currentPlayer = Lineup(
            player.battingstyle,
            player.bowlingstyle,
            player.country_id,
            player.dateofbirth,
            player.firstname,
            player.fullname,
            player.gender,
            player.id,
            player.image_path,
            player.lastname,
            null,
            null,
            null,
            null
        )

        holder.playerCard.setOnClickListener {
            val action = RankingsFragmentDirections.actionRankingsFragmentToPlayerDetailsFragment(
                currentPlayer
            )
            Navigation.findNavController(holder.itemView).navigate(action)
        }
        setAnimation(holder.itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<Data>) {
        playerList = filteredList
        notifyDataSetChanged()
    }

    fun filter(text: String) {
        Log.d(TAG, "filter: $text")
        val filteredList = ArrayList<Data>()
        for (article in list) {
            if (article.fullname?.lowercase(Locale.ROOT)
                    ?.contains(text.lowercase(Locale.ROOT)) == true
            ) {
                filteredList.add(article)
            }
        }
        filterList(filteredList)
    }

    override fun getItemCount() = playerList.size

    private fun setAnimation(viewToAnimate: View) {
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 500
        viewToAnimate.startAnimation(anim)
    }
}
