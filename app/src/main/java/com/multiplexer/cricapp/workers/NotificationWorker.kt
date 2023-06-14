package com.multiplexer.cricapp.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.multiplexer.cricapp.R

class NotificationWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        val title = inputData.getString("title")
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.cricket_bat_ball).setContentTitle(title)
            .setContentText("Less than 10 minutes left!")
            .setPriority(NotificationCompat.PRIORITY_HIGH).build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        return Result.success()
    }
}
