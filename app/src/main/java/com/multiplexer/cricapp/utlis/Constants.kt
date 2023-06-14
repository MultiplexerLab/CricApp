package com.multiplexer.cricapp.utlis

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.multiplexer.cricapp.workers.NotificationReceiver

class Constants {
    companion object {
        const val preferenceName = "cricapp"
        const val api_key = "m1tLK9vOc6ZSYB4MlTUata7LtOmRlTmWpMavc2xZGztMpkX1QIcLLFQDTFfD"
        const val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"
        const val api_token = "api_token"
        const val countries = "countries"
        const val continents = "continents"
        const val include = "include"
        const val filterStatus = "filter[status]"
        const val filterByPositionID = "filter[position_id]"
        const val filterByFields = "fields[players]"
        const val sort = "sort"
        const val fixtureById = "fixtures/{FixtureId}"
        const val fixtureId = "FixtureId"
        const val filterDate = "filter[starts_between]"
        const val filterByTeamId = "filter[localteam_id]"
        const val filterByLeagueId = "filter[league_id]"
        const val teams = "teams"
        const val league = "leagues"
        const val fixtures = "fixtures"
        const val liveFixtures = "livescores"
        const val playerById = "players/{PLAYER_ID}"
        const val players = "players"
        const val playerId = "PLAYER_ID"
        const val teamRanking = "team-rankings"
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
        intent.data = Uri.parse("${localTeam}/${visitorTeam}/${id}")
        intent.putExtra("localTeam", localTeam)
        intent.putExtra("visitorTeam", visitorTeam)
        intent.putExtra("matchId", id)
        intent.putExtra("matchRound", round)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + 10000,
            pendingIntent
        )
    }

}