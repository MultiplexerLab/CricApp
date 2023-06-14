package com.multiplexer.cricapp.adapters

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.multiplexer.cricapp.R
import com.multiplexer.cricapp.fragments.HomeFragmentDirections
import com.multiplexer.cricapp.models.fixtures.Data
import com.multiplexer.cricapp.models.fixtures.Run
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import com.multiplexer.cricapp.workers.NotificationReceiver
import com.multiplexer.cricapp.workers.NotificationWorker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class FixtureAdapter(
    private val context: Context,
    private val viewModel: CricketViewModel,
    val list: List<Data>,
    val date: String
) : RecyclerView.Adapter<FixtureAdapter.ItemViewHolder>() {
    private var tasksList = list
    private var lastPosition = -1
    private var isNightMode = false
    var team1Name = ""
    var team2Name = ""

    init {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        isNightMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leagueName: TextView = view.findViewById(R.id.leagueName)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team2Name: TextView = view.findViewById(R.id.team2Name)
        val matchNote: TextView = view.findViewById(R.id.matchNote)
        val team1Score: TextView = view.findViewById(R.id.team1Score)
        val team2Score: TextView = view.findViewById(R.id.team2Score)
        val matchRound: TextView = view.findViewById(R.id.matchRound)
        val dateAndTime: TextView = view.findViewById(R.id.dateAndTime)
        val team1Logo: ImageView = view.findViewById(R.id.team1Logo)
        val team2Logo: ImageView = view.findViewById(R.id.team2Logo)
        val cardItem: CardView = view.findViewById(R.id.cardId)
        val matchStatus: TextView = view.findViewById(R.id.matchStatus)
        val liveStatus: TextView = view.findViewById(R.id.liveStatus)
        val imageButton: ImageButton = view.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.fixtures_list_item, parent, false)
        return ItemViewHolder(root)
    }


    @SuppressLint("SimpleDateFormat", "ResourceAsColor", "UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val fixture = tasksList[position]
        val runs = fixture.runs
        val team1: MutableList<Run>
        val team2: MutableList<Run>

        if (runs != null) {
            team1 = runs.filter {
                it.team_id == fixture.localteam_id
            } as MutableList<Run>
            team2 = runs.filter {
                it.team_id == fixture.visitorteam_id
            } as MutableList<Run>

            if (team1.isNotEmpty()) {
                "${team1.lastOrNull()?.score.toString()}/${team1.lastOrNull()?.wickets.toString()} (${team1.lastOrNull()?.overs.toString()})".also {
                    holder.team1Score.text = it
                }
            } else {
                holder.team1Score.text = "N/A"
            }
            if (team2.isNotEmpty()) {
                "${team2.lastOrNull()?.score.toString()}/${team2.lastOrNull()?.wickets.toString()} (${team2.lastOrNull()?.overs.toString()})".also {
                    holder.team2Score.text = it
                }
            } else {
                holder.team2Score.text = "N/A"
            }
        } else {
            holder.team1Score.text = "N/A"
            holder.team2Score.text = "N/A"
        }

        val icon =
            if (isNightMode) R.drawable.baseline_swap_horiz_night else R.drawable.baseline_swap_horiz_day
        holder.imageButton.setImageResource(icon)


        viewModel.getImageFromTeam(fixture.localteam_id!!.toLong())
            .observe(holder.itemView.context as LifecycleOwner) {
                Picasso.get().load(it).fit().into(holder.team1Logo)
            }

        viewModel.getImageFromTeam(fixture.visitorteam_id!!.toLong())
            .observe(holder.itemView.context as LifecycleOwner) {
                Picasso.get().load(it).fit().into(holder.team2Logo)
            }

        viewModel.getLeagueName(fixture.league_id!!.toLong())
            .observe(holder.itemView.context as LifecycleOwner) {
                holder.leagueName.text = it
            }
        viewModel.getTeamName(fixture.localteam_id!!.toLong())
            .observe(holder.itemView.context as LifecycleOwner) {
                holder.team1Name.text = it
                team1Name = it
            }
        viewModel.getTeamName(fixture.visitorteam_id!!.toLong())
            .observe(holder.itemView.context as LifecycleOwner) {
                holder.team2Name.text = it
                team2Name = it
            }

        if (TextUtils.isEmpty(fixture.note)) {
            holder.matchNote.visibility = View.GONE
        } else {
            holder.matchNote.text = fixture.note
        }

        holder.matchRound.text = fixture.round

        when (fixture.status) {
            "Finished" -> {
                holder.matchStatus.text = fixture.status
                holder.liveStatus.visibility = View.GONE
            }
            "NS" -> {
                holder.matchStatus.text = context.getString(R.string.notStarted)
                holder.liveStatus.visibility = View.GONE
            }
            "Aban." -> {
                holder.matchStatus.text = context.getString(R.string.abandoned)
                holder.liveStatus.visibility = View.GONE
            }
            "Int." -> {
                holder.matchStatus.text = context.getString(R.string.interrupted)
                holder.liveStatus.visibility = View.GONE
            }
            else -> {
                holder.liveStatus.text = fixture.status
                holder.matchStatus.visibility = View.GONE
            }
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM hh:mm", Locale.getDefault())
        val dateTime = fixture.starting_at
        val date = inputFormat.parse(dateTime)
        val formattedDateTime = outputFormat.format(date)
        val targetTime = fixture.starting_at
        val formatter =
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val targetDate: Date = targetTime?.let { formatter.parse(it) } as Date
        val tenMinutesInMillis = 310 * 60 * 1000 // 10 minutes in milliseconds
        var isNotificationSent = false // initialize flag
        val countdown =
            object : CountDownTimer(targetDate.time - System.currentTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / 1000
                    val hours = timeLeft / 3600
                    val minutes = (timeLeft % 3600) / 60
                    val seconds = timeLeft % 60
                    holder.dateAndTime.text =
                        String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    if (!isNotificationSent) {
                        isNotificationSent = true
                        CoroutineScope(Dispatchers.Default).launch {
                            val inputData = androidx.work.Data.Builder().putString(
                                "title",
                                "$team1Name VS $team2Name ${fixture.round} match is about to start!"
                            ).build()
                            val notificationTimeInMillis =
                                targetDate.time - tenMinutesInMillis
                            val notificationRequest =
                                OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                                    notificationTimeInMillis, TimeUnit.MILLISECONDS
                                ).setInputData(inputData).build()
                            WorkManager.getInstance(context).enqueue(notificationRequest)
                        }
                    }

                    if (millisUntilFinished <= tenMinutesInMillis) {
                        showNotification(context, team1Name, team2Name, fixture.id, fixture.round)
                    }
                }

                override fun onFinish() {
                    holder.dateAndTime.text = formattedDateTime
                }
            }
        countdown.start()
        holder.cardItem.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToFixtureDetailsFragment(fixture)
            findNavController(holder.itemView).navigate(action)
        }
        holder.team1Logo.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToFixtureDetailsFragment(fixture)
            findNavController(holder.itemView).navigate(action)
        }
        holder.team2Logo.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToFixtureDetailsFragment(fixture)
            findNavController(holder.itemView).navigate(action)
        }

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        } else {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    fun showNotification(
        context: Context,
        localTeam: String,
        visitorTeam: String,
        id: Int?,
        round: String?
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        // Used to differentiate PendingIntents
        intent.data = Uri.parse("${localTeam}/${visitorTeam}/${id}")
        intent.putExtra("localTeam", localTeam)
        intent.putExtra("visitorTeam", visitorTeam)
        intent.putExtra("matchId", id)
        intent.putExtra("matchRound", round)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + 10000,
            pendingIntent
        )
    }
}
