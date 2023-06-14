package com.multiplexer.cricapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.multiplexer.cricapp.MainActivity
import com.multiplexer.cricapp.R

class NotificationReceiver : BroadcastReceiver() {

    private val TAG: String = "MyAlarmReceiver"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val team1 = intent.getStringExtra("localTeam")
        val team2 = intent.getStringExtra("visitorTeam")
        val matchId = intent.getIntExtra("matchId", 0)
        val matchRound = intent.getStringExtra("matchRound")
        Log.d(TAG, "onReceive: ${team1}, $team2 $matchRound")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "CricJass", "default", NotificationManager.IMPORTANCE_HIGH
            )
        )
        var contentIntent = Intent(context, MainActivity::class.java)
        val notification = NotificationCompat.Builder(context, "CricJass").setContentTitle("")
            .setContentText("$team1 vs $team2 $matchRound match will start in 10 minutes")
            .setSmallIcon(R.drawable.cricket_bat_ball).setContentIntent(
                PendingIntent.getActivity(
                    context, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE
                )
            ).setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(
                    BitmapFactory.decodeResource(
                        context.resources, R.drawable.cricket_bat_ball
                    )
                )
            ).build()
        val tag = "${team1}:${team2}:${matchId}:${matchRound}"
        notificationManager.notify(tag, 1, notification)
    }
}
