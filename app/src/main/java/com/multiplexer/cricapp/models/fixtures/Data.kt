package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Data(
    val batting: @RawValue List<Batting>?,
    val bowling: @RawValue List<Bowling>?,
    var draw_noresult: String?,
    var elected: String?,
    var first_umpire_id: Int?,
    var follow_on: Boolean?,
    var id: Int?,
    var last_period: @RawValue Any?,
    var league_id: Int?,
    var live: Boolean?,
    var localteam_dl_data: @RawValue LocalteamDlData?,
    var localteam_id: Int?,
    val manofmatch: @RawValue Manofmatch?,
    var man_of_match_id: Int?,
    var man_of_series_id: Int?,
    var note: String?,
    var referee_id: Int?,
    var resource: String?,
    var round: String?,
    var rpc_overs: String?,
    var rpc_target: String?,
    val runs: @RawValue List<Run>?,
    val lineup: @RawValue List<Lineup>?,
    val venue: @RawValue Venue?,
    var season_id: Int?,
    var second_umpire_id: Int?,
    var stage_id: Int?,
    var starting_at: String?,
    var status: String?,
    var super_over: Boolean?,
    var toss_won_team_id: Int?,
    var total_overs_played: Int?,
    var tv_umpire_id: Int?,
    var type: String?,
    var venue_id: Int?,
    var visitorteam_dl_data: @RawValue VisitorteamDlData?,
    var visitorteam_id: Int?,
    var weather_report: @RawValue List<Any>?,
    var winner_team_id: Int?
) : Parcelable