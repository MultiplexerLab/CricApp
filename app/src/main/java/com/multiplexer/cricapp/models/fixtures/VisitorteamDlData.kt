package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisitorteamDlData(
    val overs: String?,
    val score: String?,
    val total_overs_played: String?,
    val wickets_out: String?
) : Parcelable